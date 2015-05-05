
package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;
import novoda.lib.sqliteprovider.util.Constraint;

public class DatabaseAnalyzer {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";
    private static final String PRAGMA_TABLE = "PRAGMA table_info(\"%1$s\");";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";
    private static final List<String> DEFAULT_TABLES = Collections.singletonList("android_metadata");

    private final SQLiteDatabase database;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> getForeignTables(String table) {
        final Cursor cursor = executeQuery(String.format(PRAGMA_TABLE, table));
        List<String> foreignTables = createResultListForCursor(cursor);
        List<String> tables = getTableNames();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            if (name.endsWith("_id")) {
                String tableName = name.substring(0, name.lastIndexOf('_'));

                if (tables.contains(tableName + "s")) {
                    foreignTables.add(tableName + "s");
                } else if (tables.contains(tableName)) {
                    foreignTables.add(tableName);
                }
            }
        }

        cursor.close();

        return Collections.unmodifiableList(foreignTables);
    }

    private Cursor executeQuery(String query) {
        return database.rawQuery(query, null);
    }

    /**
     * @return a list of tables
     */
    public List<String> getTableNames() {
        final Cursor cursor = executeQuery(SELECT_TABLES_NAME);
        List<String> createdTable = createResultListForCursor(cursor);
        while (cursor.moveToNext()) {
            String tableName = cursor.getString(0);
            if (!DEFAULT_TABLES.contains(tableName)) {
                createdTable.add(tableName);
            }
        }
        cursor.close();
        return Collections.unmodifiableList(createdTable);
    }

    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new HashMap<String, String>();
        projection.put("_id", parent + "._id AS _id");
        addProjectionForTable(projection, parent);

        for (String foreignTable : foreignTables) {
            addProjectionForTable(projection, foreignTable);
        }

        return Collections.unmodifiableMap(projection);
    }

    private void addProjectionForTable(Map<String, String> projection, String foreignTable) {
        for (Column column : getColumns(foreignTable)) {
            String name = column.getName();
            projection.put(foreignTable + "_" + name, foreignTable + "." + name + " AS " + foreignTable + "_" + name);
        }
    }

    public List<Column> getColumns(String table) {
        final Cursor cursor = executeQuery(String.format(PRAGMA_TABLE, table));

        List<Column> columns = createResultListForCursor(cursor);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            columns.add(new Column(name, SQLiteType.valueOf(type.toUpperCase())));
        }

        cursor.close();

        return columns;
    }

    /**
     * Gets the version of SQLite used by Android.
     *
     * @return the SQLite version
     */
    public String getSQLiteVersion() {
        final Cursor cursor = executeQuery("SELECT sqlite_version() AS sqlite_version");
        String version = "";

        if (cursor.moveToFirst()) {
            version = cursor.getString(0);
        }

        cursor.close();

        return version;
    }

    public List<Constraint> getUniqueConstraints(String table) {
        final Cursor indexCursor = executeQuery(String.format(PRAGMA_INDEX_LIST, table));

        List<Constraint> constraints = createResultListForCursor(indexCursor);

        while (indexCursor.moveToNext()) {
            int isUnique = indexCursor.getInt(2);
            if (isUnique == 1) {
                String indexName = indexCursor.getString(1);
                Constraint constraint = getConstraintFromIndex(indexName);
                constraints.add(constraint);
            }
        }

        indexCursor.close();

        return constraints;
    }

    private Constraint getConstraintFromIndex(String indexName) {
        final Cursor columnCursor = executeQuery(String.format(PRAGMA_INDEX_INFO, indexName));
        List<String> columns = createResultListForCursor(columnCursor);
        while (columnCursor.moveToNext()) {
            String columnName = columnCursor.getString(2);
            columns.add(columnName);
        }
        columnCursor.close();
        return new Constraint(columns);
    }

    private <T> List<T> createResultListForCursor(Cursor cursor) {
        return new ArrayList<>(cursor.getCount());
    }
}


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
    private static final String PRAGMA_TABLE_INFO = "PRAGMA table_info(\"%1$s\");";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";
    private static final List<String> DEFAULT_TABLES = Collections.singletonList("android_metadata");
    private static final String ID_COLUMN_NAME = "_id";

    private final SQLiteDatabase database;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> getForeignTables(String table) {
        final List<String> tables = getTableNames();
        return getDataForQuery(String.format(PRAGMA_TABLE_INFO, table),
                new RowParser<String>() {
                    @Override
                    public String parseRow(Cursor cursor) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        if (name.endsWith(ID_COLUMN_NAME)) {
                            String tableName = name.substring(0, name.lastIndexOf('_'));

                            if (tables.contains(tableName + "s")) {
                                return tableName + "s";
                            } else if (tables.contains(tableName)) {
                                return tableName;
                            }
                        }
                        return null;
                    }
                });
    }

    private Cursor executeQuery(String query) {
        return database.rawQuery(query, null);
    }

    /**
     * @return a list of tables
     */
    public List<String> getTableNames() {
        return getDataForQuery(SELECT_TABLES_NAME,
                new RowParser<String>() {
                    @Override
                    public String parseRow(Cursor cursor) {
                        String tableName = cursor.getString(0);
                        if (DEFAULT_TABLES.contains(tableName)) {
                            return null;
                        }
                        return tableName;
                    }
                });
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

    private void addProjectionForTable(Map<String, String> projection, String table) {
        for (Column column : getColumns(table)) {
            String columnName = column.getName();
            addProjection(projection, table, columnName);
        }
    }

    private String addProjection(Map<String, String> projection, String table, String column) {
        return projection.put(table + "_" + column, table + "." + column + " AS " + table + "_" + column);
    }

    public List<Column> getColumns(String table) {

        return getDataForQuery(String.format(PRAGMA_TABLE_INFO, table),
                new RowParser<Column>() {
                    @Override
                    public Column parseRow(Cursor cursor) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                        return new Column(name, SQLiteType.valueOf(type.toUpperCase()));
                    }
                });
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
        return getDataForQuery(String.format(PRAGMA_INDEX_LIST, table),
                new RowParser<Constraint>() {
                    @Override
                    public Constraint parseRow(Cursor cursor) {
                        int isUnique = cursor.getInt(2);
                        if (isUnique == 1) {
                            String indexName = cursor.getString(1);
                            return getConstraintFromIndex(indexName);
                        }
                        return null;
                    }
                });
    }

    private Constraint getConstraintFromIndex(String indexName) {
        List<String> columns = getDataForQuery(String.format(PRAGMA_INDEX_INFO, indexName),
                new RowParser<String>() {
                    @Override
                    public String parseRow(Cursor cursor) {
                        String columnName = cursor.getString(2);
                        return columnName;
                    }
                });

        return new Constraint(columns);
    }

    private <T> List<T> getDataForQuery(String query, RowParser<T> parser) {
        final Cursor cursor = executeQuery(query);

        List<T> items = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            final T item = parser.parseRow(cursor);
            if (item != null) {
                items.add(item);
            }
        }

        cursor.close();

        return Collections.unmodifiableList(items);
    }

    private interface RowParser<T> {
        T parseRow(Cursor cursor);
    }
}

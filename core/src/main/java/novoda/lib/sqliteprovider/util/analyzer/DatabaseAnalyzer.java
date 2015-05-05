
package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.util.Constraint;

public class DatabaseAnalyzer {

    private final String PRAGMA_TABLE_INFO = "PRAGMA table_info(\"%1$s\");";
    private final String ID_COLUMN_NAME = "_id";

    private final SQLiteDatabase database;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> getForeignTables(final String table) {
        final List<String> tables = getTableNames();
        return getDataForQuery(new Query<String>() {
            @Override
            public String getSqlStatement() {
                return String.format(PRAGMA_TABLE_INFO, table);
            }

            @Override
            public String parseRow(Cursor cursor) {
                String name = getTableName(cursor);
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

            private String getTableName(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndexOrThrow("name"));
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
        return getDataForQuery(new TableNameQuery());
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

    public List<Column> getColumns(final String table) {
        return getDataForQuery(new TableColumnQuery(table));
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

    public List<Constraint> getUniqueConstraints(final String table) {
        return getDataForQuery(new TableConstraintQuery(table));
    }

    private Constraint getConstraintFromIndex(String indexName) {
        List<String> columns = getDataForQuery(new IndexColumnQuery(indexName));

        return new Constraint(columns);
    }

    private <T> List<T> getDataForQuery(Query<T> query) {
        final Cursor cursor = executeQuery(query.getSqlStatement());

        List<T> items = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            final T item = query.parseRow(cursor);
            if (item != null) {
                items.add(item);
            }
        }

        cursor.close();

        return Collections.unmodifiableList(items);
    }

    private class TableConstraintQuery implements Query<Constraint> {
        private final String table;

        public TableConstraintQuery(String table) {
            this.table = table;
        }

        @Override
        public String getSqlStatement() {
            return String.format("PRAGMA index_list('%1$s');", table);
        }

        @Override
        public Constraint parseRow(Cursor cursor) {
            if (isUniqueConstraint(cursor)) {
                String indexName = getIndexName(cursor);
                return getConstraintFromIndex(indexName);
            }
            return null;
        }

        private String getIndexName(Cursor cursor) {
            return cursor.getString(1);
        }

        private boolean isUniqueConstraint(Cursor cursor) {
            return cursor.getInt(2) == 1;
        }
    }
}

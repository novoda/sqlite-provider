package novoda.lib.sqliteprovider.analyzer;

import android.database.Cursor;

import java.util.List;

class ForeignTablesQuery implements Query<String> {

    private final String table;
    private final List<String> allDatabaseTables;

    public ForeignTablesQuery(String table, List<String> allDatabaseTables) {
        this.table = table;
        this.allDatabaseTables = allDatabaseTables;
    }

    @Override
    public String getSqlStatement() {
        return "PRAGMA table_info(\"" + table + "\");";
    }

    @Override
    public String parseRow(Cursor cursor) {
        String name = getTableName(cursor);
        String idColumnName = "_id";
        if (name.endsWith(idColumnName)) {
            String tableName = name.substring(0, name.lastIndexOf('_'));

            if (allDatabaseTables.contains(tableName + "s")) {
                return tableName + "s";
            } else if (allDatabaseTables.contains(tableName)) {
                return tableName;
            }
        }
        return null;
    }

    private String getTableName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow("name"));
    }
}

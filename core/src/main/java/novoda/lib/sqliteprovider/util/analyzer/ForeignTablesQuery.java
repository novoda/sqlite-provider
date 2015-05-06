package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;

import java.util.List;

class ForeignTablesQuery implements Query<String> {

    private final String table;
    private final List<String> tables;

    public ForeignTablesQuery(String table, List<String> tables) {
        this.table = table;
        this.tables = tables;
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
}

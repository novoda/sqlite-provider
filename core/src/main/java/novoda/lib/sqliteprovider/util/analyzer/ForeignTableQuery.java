package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;

import java.util.List;

class ForeignTableQuery implements Query<String> {

    private final String table;
    private final List<String> tables;

    public ForeignTableQuery(String table, List<String> tables) {
        this.table = table;
        this.tables = tables;
    }

    @Override
    public String getSqlStatement() {
        String PRAGMA_TABLE_INFO = "PRAGMA table_info(\"%1$s\");";
        return String.format(PRAGMA_TABLE_INFO, table);
    }

    @Override
    public String parseRow(Cursor cursor) {
        String name = getTableName(cursor);
        String ID_COLUMN_NAME = "_id";
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
}

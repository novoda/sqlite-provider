package novoda.lib.sqliteprovider.analyzer;

import android.database.Cursor;

class TableUniqueConstraintsQuery implements Query<String> {
    private final String table;

    public TableUniqueConstraintsQuery(String table) {
        this.table = table;
    }

    @Override
    public String getSqlStatement() {
        return String.format("PRAGMA index_list('%1$s');", table);
    }

    @Override
    public String parseRow(Cursor cursor) {
        if (isUniqueConstraint(cursor)) {
            return getIndexName(cursor);
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

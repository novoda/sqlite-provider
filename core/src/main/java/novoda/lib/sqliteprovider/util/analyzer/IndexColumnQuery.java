package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;

class IndexColumnQuery implements Query<String> {

    private final String indexName;

    public IndexColumnQuery(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public String getSqlStatement() {
        return "PRAGMA index_info('" + indexName + "');";
    }

    @Override
    public String parseRow(Cursor cursor) {
        return getColumnName(cursor);
    }

    private String getColumnName(Cursor cursor) {
        return cursor.getString(2);
    }
}

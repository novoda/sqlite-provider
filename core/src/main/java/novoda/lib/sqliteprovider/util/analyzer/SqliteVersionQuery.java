package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;

class SqliteVersionQuery implements Query<String> {
    @Override
    public String getSqlStatement() {
        return "SELECT sqlite_version() AS sqlite_version";
    }

    @Override
    public String parseRow(Cursor cursor) {
        return getVersionString(cursor);
    }

    private String getVersionString(Cursor cursor) {
        return cursor.getString(0);
    }
}

package novoda.lib.sqliteprovider.analyzer;

import android.database.Cursor;

import java.util.Collections;
import java.util.List;

class DatabaseTablesQuery implements Query<String> {
    private static final List<String> DEFAULT_TABLES = Collections.singletonList("android_metadata");

    @Override
    public String getSqlStatement() {
        return "SELECT name FROM sqlite_master WHERE type='table';";
    }

    @Override
    public String parseRow(Cursor cursor) {
        String tableName = cursor.getString(0);
        if (DEFAULT_TABLES.contains(tableName)) {
            return null;
        }
        return tableName;
    }
}

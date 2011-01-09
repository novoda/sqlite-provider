
package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBUtils {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";

    private static final String PRAGMA_TABLE = "PRAGMA table_info(\"$s\");";

    static public List<String> getForeignTables(SQLiteDatabase db, String table) {
        final Cursor cur = db.rawQuery(String.format(PRAGMA_TABLE, table), null);
        List<String> tables = getTables(db);
        List<String> foreignTables = new ArrayList<String>(5);
        String name;
        String tableName;
        while (cur.moveToNext()) {
            name = cur.getString(cur.getColumnIndexOrThrow("name"));
            if (name.endsWith("_id")) {
                tableName = name.substring(0, name.lastIndexOf("_"));
                if (tables.contains(tableName + "s")) {
                    foreignTables.add(tableName + "s");
                } else if (tables.contains(tableName)) {
                    foreignTables.add(tableName);
                }
            }
        }
        cur.close();
        return Collections.unmodifiableList(foreignTables);
    }

    /**
     * @param db the database to get meta information from
     * @return a list of tables
     */
    static public List<String> getTables(SQLiteDatabase db) {
        final Cursor cur = db.rawQuery(SELECT_TABLES_NAME, null);
        List<String> createdTable = new ArrayList<String>(cur.getCount());
        while (cur.moveToNext()) {
            createdTable.add(cur.getString(0));
        }
        cur.close();
        return Collections.unmodifiableList(createdTable);
    }

    /**
     * Gets the version of SQLite used by Android.
     * 
     * @return the SQLite version
     */
    public static String getSQLiteVersion() {
        final Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null).rawQuery(
                "select sqlite_version() AS sqlite_version", null);
        String sqliteVersion = "";
        while (cursor.moveToNext()) {
            sqliteVersion += cursor.getString(0);
        }
        cursor.close();
        return sqliteVersion;
    }
}


package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

import java.util.*;
import java.util.Map.Entry;

public final class DBUtils {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";

    private static final String PRAGMA_TABLE = "PRAGMA table_info(\"%1$s\");";

    private static final String PRGAMA_INDEX_LIST = "PRAGMA index_list('%1$s');";

    private static final String PRGAMA_INDEX_INFO = "PRAGMA index_info('%1$s');";

    private static List<String> defaultTables = Arrays.asList(new String[] {
            "android_metadata"
    });

    private DBUtils() {
        // Util class
    }

    public static List<String> getForeignTables(SQLiteDatabase db, String table) {
        final Cursor cur = db.rawQuery(String.format(PRAGMA_TABLE, table), null);
        List<String> tables = getTables(db);
        List<String> foreignTables = new ArrayList<String>(5);
        String name;
        String tableName;
        while (cur.moveToNext()) {
            name = cur.getString(cur.getColumnIndexOrThrow("name"));
            if (name.endsWith("_id")) {
                tableName = name.substring(0, name.lastIndexOf('_'));
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
    public static List<String> getTables(SQLiteDatabase db) {
        final Cursor cur = db.rawQuery(SELECT_TABLES_NAME, null);
        List<String> createdTable = new ArrayList<String>(cur.getCount());
        String tableName;
        while (cur.moveToNext()) {
            tableName = cur.getString(0);
            if (!defaultTables.contains(tableName)) {
                createdTable.add(tableName);
            }
        }
        cur.close();
        return Collections.unmodifiableList(createdTable);
    }

    public static Map<String, String> getProjectionMap(SQLiteDatabase db, String parent, String... foreignTables) {
        Map<String, String> projection = new HashMap<String, String>();
        projection.put("_id", parent + "._id AS _id");
        for (Entry<String, SQLiteType> entry : getFields(db, parent).entrySet()) {
            projection.put(parent + "_" + entry.getKey(), parent + "." + entry.getKey() + " AS "
                    + parent + "_" + entry.getKey());
        }

        for (String ft : foreignTables) {
            for (Entry<String, SQLiteType> entry : getFields(db, ft).entrySet()) {
                projection.put(ft + "_" + entry.getKey(), ft + "." + entry.getKey() + " AS " + ft
                        + "_" + entry.getKey());
            }
        }
        return Collections.unmodifiableMap(projection);
    }

    public static Map<String, SQLiteType> getFields(SQLiteDatabase db, String table) {
        final Cursor cur = db.rawQuery(String.format(PRAGMA_TABLE, table), null);
        Map<String, SQLiteType> fields = new HashMap<String, SQLiteType>(cur.getCount());
        String name;
        String type;
        while (cur.moveToNext()) {
            name = cur.getString(cur.getColumnIndexOrThrow("name"));
            type = cur.getString(cur.getColumnIndexOrThrow("type"));
            fields.put(name, SQLiteType.valueOf(type.toUpperCase()));
        }
        cur.close();
        return Collections.unmodifiableMap(fields);
    }

    /**
     * Gets the version of SQLite used by Android.
     *
     * @return the SQLite version
     */
    public static String getSQLiteVersion() {
        final Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null).rawQuery(
                "select sqlite_version() AS sqlite_version", null);
        StringBuilder sqliteVersion = new StringBuilder();
        while (cursor.moveToNext()) {
            sqliteVersion.append(cursor.getString(0));
        }
        cursor.close();
        return sqliteVersion.toString();
    }

    public static List<String> getUniqueConstrains(SQLiteDatabase db, String table) {
        List<String> constrains = new ArrayList<String>();
        final Cursor pragmas = db.rawQuery(String.format(PRGAMA_INDEX_LIST, table), null);
        while (pragmas.moveToNext()) {
            int isUnique = pragmas.getInt(2);
            if (isUnique == 1) {
                String name = pragmas.getString(1);
                final Cursor pragmaInfo = db.rawQuery(String.format(PRGAMA_INDEX_INFO, name), null);
                if (pragmaInfo.moveToFirst()) {
                    constrains.add(pragmaInfo.getString(2));
                }
                pragmaInfo.close();
            }
        }
        pragmas.close();
        return constrains;
    }
}

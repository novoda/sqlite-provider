
package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

public final class DBUtils {

    private static final String PRGAMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRGAMA_INDEX_INFO = "PRAGMA index_info('%1$s');";

    private DBUtils() {
        // Util class
    }

    public static List<String> getForeignTables(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).getForeignTables(table);
    }

    /**
     * Deprecated. Use {@link DatabaseStructure} instead
     *
     * @param db the database to get meta information from
     * @return a list of tables
     */
    @Deprecated
    public static List<String> getTables(SQLiteDatabase db) {
        return new DatabaseStructure(db).getTables();
    }

    /**
     * Deprecated. Use {@link DatabaseStructure} instead
     */
    @Deprecated
    public static Map<String, String> getProjectionMap(SQLiteDatabase db, String parent, String... foreignTables) {
        return new DatabaseStructure(db).getProjectionMap(parent, foreignTables);
    }

    /**
     * Deprecated. Use {@link DatabaseStructure} instead
     */
    @Deprecated
    public static Map<String, SQLiteType> getFields(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).getFields(table);
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


    /**
     * Use {@link #getUniqueConstraints(SQLiteDatabase, String)}
     */
    @Deprecated
    public static List<String> getUniqueConstrains(SQLiteDatabase db, String table) {
        List<String> constrains = new ArrayList<>();
        final Cursor indices = queryIndexListForTable(table, db);
        while (indices.moveToNext()) {
            int isUnique = indices.getInt(2);
            if (isUnique == 1) {
                String indexName = indices.getString(1);
                final Cursor pragmaInfo = queryIndexInfo(indexName, db);
                if (pragmaInfo.moveToFirst()) {
                    constrains.add(pragmaInfo.getString(2));
                }
                pragmaInfo.close();
            }
        }
        indices.close();
        return constrains;
    }

    /**
     * Deprecated. Use {@link DatabaseStructure} instead
     */
    @Deprecated
    public static List<Constraint> getUniqueConstraints(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).getUniqueConstraints(table);
    }

    private static Cursor queryIndexListForTable(String table, SQLiteDatabase database) {
        return database.rawQuery(String.format(PRGAMA_INDEX_LIST, table), null);
    }

    private static Cursor queryIndexInfo(String index, SQLiteDatabase database) {
        return database.rawQuery(String.format(PRGAMA_INDEX_INFO, index), null);
    }

}

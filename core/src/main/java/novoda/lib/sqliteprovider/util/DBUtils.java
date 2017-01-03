
package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

public final class DBUtils {

    private DBUtils() {
        // Util class
    }

    /**
     * Deprecated. Use {@link DatabaseStructure#foreignTables(String)} instead
     */
    public static List<String> getForeignTables(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).foreignTables(table);
    }

    /**
     * Deprecated. Use {@link DatabaseStructure#tables()} instead
     *
     * @param db the database to get meta information from
     * @return a list of tables
     */
    @Deprecated
    public static List<String> getTables(SQLiteDatabase db) {
        return new DatabaseStructure(db).tables();
    }

    /**
     * Deprecated. Use {@link DatabaseStructure#projectionMap(String, String...)} instead
     */
    @Deprecated
    public static Map<String, String> getProjectionMap(SQLiteDatabase db, String parent, String... foreignTables) {
        return new DatabaseStructure(db).projectionMap(parent, foreignTables);
    }

    /**
     * Deprecated. Use {@link DatabaseStructure#columns(String)} instead
     */
    @Deprecated
    public static Map<String, SQLiteType> getFields(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).columns(table);
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
     * Deprecated. Use {@link DatabaseStructure#uniqueConstraints(String)} instead
     */
    @Deprecated
    public static List<Constraint> getUniqueConstraints(SQLiteDatabase db, String table) {
        return new DatabaseStructure(db).uniqueConstraints(table);
    }

}

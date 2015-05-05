package novoda.lib.sqliteprovider.util;

import android.content.ContentValues;

import novoda.rest.database.SQLiteTableCreator;

public class DatabaseUtils extends android.database.DatabaseUtils {

    /**
     * @deprecated
     * Use {@link DBUtils#contentValuestoTableCreate(ContentValues, String)} instead
     *
     */
    @Deprecated
    public static String contentValuestoTableCreate(ContentValues values, String table) {
        return DBUtils.contentValuestoTableCreate(values, table);
    }

    /**
     * @deprecated Use {@link DBUtils#getCreateStatement(SQLiteTableCreator)} instead
     */
    @Deprecated
    public static String getCreateStatement(SQLiteTableCreator creator) {
        return DBUtils.getCreateStatement(creator);
    }

    /**
     * @deprecated Use {@link DBUtils#getSQLiteVersion()} instead
     */
    @Deprecated
    public static String getSQLiteVersion() {
        return DBUtils.getSQLiteVersion();
    }
}

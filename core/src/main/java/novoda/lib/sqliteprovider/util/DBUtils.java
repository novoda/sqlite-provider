
package novoda.lib.sqliteprovider.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;
import novoda.lib.sqliteprovider.util.analyzer.DatabaseAnalyzer;
import novoda.lib.sqliteprovider.util.analyzer.StatementGenerator;
import novoda.rest.database.SQLiteTableCreator;

@Deprecated
public final class DBUtils {

    private static final String PRAGMA_TABLE = "PRAGMA table_info(\"%1$s\");";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";

    private DBUtils() {
        // Util class
    }

    /**
     * @deprecated Use {@link DatabaseAnalyzer#getForeignTables(String)}   instead
     */
    @Deprecated
    public static List<String> getForeignTables(SQLiteDatabase db, String table) {
        return new DatabaseAnalyzer(db).getForeignTables(table);
    }

    /**
     * @param db the database to get meta information from
     * @return a list of tables
     * @deprecated Use {@link DatabaseAnalyzer#getTableNames()}
     */
    @Deprecated
    public static List<String> getTables(SQLiteDatabase db) {
        return new DatabaseAnalyzer(db).getTableNames();
    }

    /**
     * @deprecated Use {@link DatabaseAnalyzer#getProjectionMap(String, String...)}  instead
     */
    @Deprecated
    public static Map<String, String> getProjectionMap(SQLiteDatabase db, String parent, String... foreignTables) {
        return new DatabaseAnalyzer(db).getProjectionMap(parent, foreignTables);
    }

    /**
     * @deprecated Use {@link DatabaseAnalyzer#getColumns(String)} instead
     */
    @Deprecated
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
     * @deprecated Use {@link DatabaseAnalyzer#getSQLiteVersion()} instead
     */
    @Deprecated
    public static String getSQLiteVersion() {
        final SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        return new DatabaseAnalyzer(sqLiteDatabase).getSQLiteVersion();
    }

    /**
     * @deprecated Use {@link DatabaseAnalyzer#getUniqueConstraints(String)} )} instead
     */
    @Deprecated
    public static List<String> getUniqueConstrains(SQLiteDatabase db, String table) {
        List<String> constrains = new ArrayList<String>();
        final Cursor pragmas = db.rawQuery(String.format(PRAGMA_INDEX_LIST, table), null);
        while (pragmas.moveToNext()) {
            int isUnique = pragmas.getInt(2);
            if (isUnique == 1) {
                String name = pragmas.getString(1);
                final Cursor pragmaInfo = db.rawQuery(String.format(PRAGMA_INDEX_INFO, name), null);
                if (pragmaInfo.moveToFirst()) {
                    constrains.add(pragmaInfo.getString(2));
                }
                pragmaInfo.close();
            }
        }
        pragmas.close();
        return constrains;
    }

    /**
     * @deprecated Use {@link DatabaseAnalyzer#getUniqueConstraints(String)} instead
     */
    @Deprecated
    public static List<Constraint> getUniqueConstraints(SQLiteDatabase db, String table) {
        return new DatabaseAnalyzer(db).getUniqueConstraints(table);
    }

    /**
     * @deprecated Use {@link StatementGenerator#contentValuestoTableCreate(ContentValues, String)} instead
     */
    @Deprecated
    public static String contentValuestoTableCreate(ContentValues values, String table) {
        return new StatementGenerator().contentValuestoTableCreate(values, table);
    }

    /**
     * @deprecated Use {@link StatementGenerator#getCreateStatement(SQLiteTableCreator)} instead
     */
    @Deprecated
    public static String getCreateStatement(SQLiteTableCreator creator) {
        return new StatementGenerator().getCreateStatement(creator);
    }
}

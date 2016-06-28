
package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

public final class DBUtils {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";

    private static final String PRAGMA_TABLE_INFO = "PRAGMA table_info('%1$s');";

    private static final String PRGAMA_INDEX_LIST = "PRAGMA index_list('%1$s');";

    private static final String PRGAMA_INDEX_INFO = "PRAGMA index_info('%1$s');";

    private static final String COLUMN_PRIMARY_KEY = "pk";

    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_TYPE = "type";

    private static List<String> defaultTables = Arrays.asList(new String[]{
            "android_metadata"
    });

    private DBUtils() {
        // Util class
    }

    public static List<String> getForeignTables(SQLiteDatabase db, String table) {
        final Cursor cur = queryTableColumnsFor(table, db);
        List<String> tables = getTables(db);
        List<String> foreignTables = new ArrayList<String>(5);
        String name;
        String tableName;
        while (cur.moveToNext()) {
            name = cur.getString(cur.getColumnIndexOrThrow(COLUMN_NAME));
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
        final Cursor cursor = queryTableColumnsFor(table, db);
        Map<String, SQLiteType> fields = new HashMap<String, SQLiteType>(cursor.getCount());
        String name;
        String type;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
            fields.put(name, SQLiteType.fromName(type));
        }
        cursor.close();
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


    /**
     * Use {@link #getUniqueConstraints(SQLiteDatabase, String)}
     */
    @Deprecated
    public static List<String> getUniqueConstrains(SQLiteDatabase db, String table) {
        List<String> constrains = new ArrayList<String>();
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


    public static List<Constraint> getUniqueConstraints(SQLiteDatabase db, String table) {
        List<Constraint> constraints = new ArrayList<Constraint>();

        // This is an implicit unique index, and won't show up querying the other indexes
        Constraint integerPrimaryKeyConstraint = findIntegerPrimaryKeyConstraint(db, table);
        if (integerPrimaryKeyConstraint != null) {
            constraints.add(integerPrimaryKeyConstraint);
        }

        final Cursor indexCursor = queryIndexListForTable(table, db);
        while (indexCursor.moveToNext()) {
            int isUnique = indexCursor.getInt(2);
            if (isUnique == 1) {
                String indexName = indexCursor.getString(1);
                final Cursor columnCursor = queryIndexInfo(indexName, db);
                List<String> columns = new ArrayList<>(columnCursor.getCount());
                while (columnCursor.moveToNext()) {
                    String columnName = columnCursor.getString(2);
                    columns.add(columnName);
                }
                columnCursor.close();
                constraints.add(new Constraint(columns));
            }
        }
        indexCursor.close();
        return constraints;
    }

    private static Constraint findIntegerPrimaryKeyConstraint(SQLiteDatabase database, String table) {
        final Cursor cursor = queryTableColumnsFor(table, database);
        try {
            while (cursor.moveToNext()) {
                if (isTableInfoItemAnIntegerPrimaryKey(cursor)) {
                    String columnName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    return new Constraint(Collections.singletonList(columnName));
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private static Cursor queryTableColumnsFor(String table, SQLiteDatabase database) {
        return database.rawQuery(String.format(PRAGMA_TABLE_INFO, table), null);
    }

    private static Cursor queryIndexListForTable(String table, SQLiteDatabase database) {
        return database.rawQuery(String.format(PRGAMA_INDEX_LIST, table), null);
    }

    private static Cursor queryIndexInfo(String index, SQLiteDatabase database) {
        return database.rawQuery(String.format(PRGAMA_INDEX_INFO, index), null);
    }

    private static boolean isTableInfoItemAnIntegerPrimaryKey(Cursor cursor) {
        int pkInt = cursor.getInt(cursor.getColumnIndex(COLUMN_PRIMARY_KEY));
        String columnType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

        boolean isPrimaryKey = pkInt == 1;
        boolean isInteger = SQLiteType.fromName(columnType) == SQLiteType.INTEGER;

        return isPrimaryKey && isInteger;
    }

}

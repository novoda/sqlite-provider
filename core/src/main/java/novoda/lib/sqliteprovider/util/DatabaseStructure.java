package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

public class DatabaseStructure {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";
    private static final String PRAGMA_TABLE_INFO = "PRAGMA table_info('%1$s');";
    private static final String PRGAMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRGAMA_INDEX_INFO = "PRAGMA index_info('%1$s');";
    private static final String COLUMN_PRIMARY_KEY = "pk";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TYPE = "type";

    private static final List<String> DEFAULT_TABLES = Collections.singletonList("android_metadata");

    private final SQLiteDatabase database;

    public DatabaseStructure(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * @return a list of tables
     */
    public List<String> getTables() {
        final Cursor cur = database.rawQuery(SELECT_TABLES_NAME, null);
        List<String> createdTable = new ArrayList<>(cur.getCount());
        String tableName;
        while (cur.moveToNext()) {
            tableName = cur.getString(0);
            if (!DEFAULT_TABLES.contains(tableName)) {
                createdTable.add(tableName);
            }
        }
        cur.close();
        return Collections.unmodifiableList(createdTable);
    }

    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new HashMap<>();
        projection.put("_id", parent + "._id AS _id");
        for (Map.Entry<String, SQLiteType> entry : getFields(parent).entrySet()) {
            projection.put(parent + "_" + entry.getKey(), parent + "." + entry.getKey() + " AS "
                    + parent + "_" + entry.getKey());
        }
        for (String ft : foreignTables) {
            for (Map.Entry<String, SQLiteType> entry : getFields(ft).entrySet()) {
                projection.put(ft + "_" + entry.getKey(), ft + "." + entry.getKey() + " AS " + ft
                        + "_" + entry.getKey());
            }
        }
        return Collections.unmodifiableMap(projection);
    }

    public List<String> getForeignTables(String table) {
        final Cursor cur = queryTableColumnsFor(table);
        List<String> tables = getTables();
        List<String> foreignTables = new ArrayList<>(5);
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

    public Map<String, SQLiteType> getFields(String table) {
        final Cursor cursor = queryTableColumnsFor(table);
        Map<String, SQLiteType> fields = new HashMap<>(cursor.getCount());
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

    private Cursor queryTableColumnsFor(String table) {
        return database.rawQuery(String.format(PRAGMA_TABLE_INFO, table), null);
    }

    public List<Constraint> getUniqueConstraints(String table) {
        List<Constraint> constraints = new ArrayList<>();

        // This is an implicit unique index, and won't show up querying the other indexes
        Constraint integerPrimaryKeyConstraint = findIntegerPrimaryKeyConstraint(table);
        if (integerPrimaryKeyConstraint != null) {
            constraints.add(integerPrimaryKeyConstraint);
        }

        final Cursor indexCursor = queryIndexListForTable(table);
        while (indexCursor.moveToNext()) {
            int isUnique = indexCursor.getInt(2);
            if (isUnique == 1) {
                String indexName = indexCursor.getString(1);
                final Cursor columnCursor = queryIndexInfo(indexName);
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

    private Constraint findIntegerPrimaryKeyConstraint(String table) {
        final Cursor cursor = queryTableColumnsFor(table);
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

    private static boolean isTableInfoItemAnIntegerPrimaryKey(Cursor cursor) {
        int pkInt = cursor.getInt(cursor.getColumnIndex(COLUMN_PRIMARY_KEY));
        String columnType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

        // NOT A BOOLEAN AS INTEGER: 0 if not part of PK, otherwise index of column in key: https://www.sqlite.org/pragma.html#pragma_table_info
        boolean isPrimaryKey = pkInt != 0;
        boolean isInteger = SQLiteType.fromName(columnType) == SQLiteType.INTEGER;

        return isPrimaryKey && isInteger;
    }

    private Cursor queryIndexListForTable(String table) {
        return database.rawQuery(String.format(PRGAMA_INDEX_LIST, table), null);
    }

    private Cursor queryIndexInfo(String index) {
        return database.rawQuery(String.format(PRGAMA_INDEX_INFO, index), null);
    }

}

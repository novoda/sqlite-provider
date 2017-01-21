package novoda.lib.sqliteprovider.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import novoda.lib.sqliteprovider.sqlite.DatabaseMetaInfo.SQLiteType;

public class DatabaseStructure {

    private static final String SQLITE_MASTER_TABLE_NAME = "sqlite_master";
    private static final String SELECTION_TYPE_TABLE = "type='table'";
    private static final String PRAGMA_TABLE_INFO = "PRAGMA table_info('%1$s');";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";
    private static final String COLUMN_PRIMARY_KEY_INDEX = "pk";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TYPE = "type";
    private static final String ANDROID_METADATA_TABLE_NAME = "android_metadata";

    private final SQLiteDatabase database;

    public DatabaseStructure(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> tables() {
        Cursor tablesCursor = database.query(SQLITE_MASTER_TABLE_NAME, new String[]{COLUMN_NAME}, SELECTION_TYPE_TABLE, null, null, null, null);
        List<String> tables = parseTablesFrom(tablesCursor);
        tablesCursor.close();
        return Collections.unmodifiableList(tables);
    }

    private List<String> parseTablesFrom(Cursor tablesCursor) {
        List<String> createdTables = new ArrayList<>(tablesCursor.getCount());
        while (tablesCursor.moveToNext()) {
            String tableName = tablesCursor.getString(0);
            createdTables.add(tableName);
        }
        createdTables.remove(ANDROID_METADATA_TABLE_NAME);
        return createdTables;
    }

    public Map<String, String> projectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new TreeMap<>();
        projection.put("_id", parent + "._id AS _id");
        projection.putAll(projectionFor(parent, columnsFor(parent)));
        projection.putAll(projectionFor(foreignTables));
        return Collections.unmodifiableMap(projection);
    }

    private static Map<String, String> projectionFor(String table, Map<String, SQLiteType> fields) {
        Map<String, String> projection = new TreeMap<>();
        for (Map.Entry<String, SQLiteType> entry : fields.entrySet()) {
            projection.put(table + "_" + entry.getKey(), table + "." + entry.getKey() + " AS "
                    + table + "_" + entry.getKey());
        }
        return projection;
    }

    public Map<String, SQLiteType> columnsFor(String table) {
        Cursor columnsCursor = queryTableColumnsFor(table);
        Map<String, SQLiteType> columns = parseColumnsFrom(columnsCursor);
        columnsCursor.close();
        return Collections.unmodifiableMap(columns);
    }

    private Cursor queryTableColumnsFor(String table) {
        return database.rawQuery(String.format(PRAGMA_TABLE_INFO, table), null);
    }

    private Map<String, SQLiteType> parseColumnsFrom(Cursor columnsCursor) {
        Map<String, SQLiteType> columns = new HashMap<>(columnsCursor.getCount());
        while (columnsCursor.moveToNext()) {
            String name = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_NAME));
            String type = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_TYPE));
            columns.put(name, SQLiteType.fromName(type));
        }
        return columns;
    }

    private Map<String, String> projectionFor(String... foreignTables) {
        Map<String, String> projection = new TreeMap<>();
        for (String ft : foreignTables) {
            projection.putAll(projectionFor(ft, columnsFor(ft)));
        }
        return projection;
    }

    public List<String> foreignTablesFor(String table) {
        Cursor columnsCursor = queryTableColumnsFor(table);
        List<String> allTables = tables();
        List<String> foreignTables = parseForeignTablesFor(columnsCursor, allTables);
        columnsCursor.close();
        return Collections.unmodifiableList(foreignTables);
    }

    private List<String> parseForeignTablesFor(Cursor columnsCursor, List<String> allTables) {
        List<String> foreignTables = new ArrayList<>();
        while (columnsCursor.moveToNext()) {
            String columnName = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_NAME));
            String foreignTable = foreignTableFor(columnName, allTables);
            if (foreignTable != null) {
                foreignTables.add(foreignTable);
            }
        }
        return foreignTables;
    }

    private String foreignTableFor(String columnName, List<String> tables) {
        if (!isForeignKey(columnName)) {
            return null;
        }
        String tableName = columnName.substring(0, columnName.lastIndexOf('_'));
        if (tables.contains(tableName + "s")) {
            return (tableName + "s");
        } else if (tables.contains(tableName)) {
            return (tableName);
        }
        return null;
    }

    private boolean isForeignKey(String columnName) {
        return columnName.endsWith("_id");
    }

    public List<Constraint> uniqueConstraintsFor(String table) {
        List<Constraint> uniqueConstraints = new ArrayList<>();
        Constraint implicitIntegerPrimaryKeyConstraint = implicitIntegerPrimaryKeyConstraint(table);
        if (implicitIntegerPrimaryKeyConstraint != null) {
            uniqueConstraints.add(implicitIntegerPrimaryKeyConstraint);
        }
        Cursor indexCursor = queryIndexListFor(table);
        uniqueConstraints.addAll(parseUniqueConstraintsFrom(indexCursor));
        indexCursor.close();
        return Collections.unmodifiableList(uniqueConstraints);
    }

    private Constraint implicitIntegerPrimaryKeyConstraint(String table) {
        // This is an implicit unique index, and won't show up querying the other indexes
        Cursor columnsCursor = queryTableColumnsFor(table);
        try {
            return parseIntegerPrimaryKeyConstraintFrom(columnsCursor);
        } finally {
            columnsCursor.close();
        }
    }

    private Constraint parseIntegerPrimaryKeyConstraintFrom(Cursor columnsCursor) {
        Constraint integerPrimaryKeyConstraint = null;
        while (columnsCursor.moveToNext() && integerPrimaryKeyConstraint == null) {
            integerPrimaryKeyConstraint = extractIntegerPrimaryKeyConstraintFrom(columnsCursor);
        }
        return integerPrimaryKeyConstraint;
    }

    private Constraint extractIntegerPrimaryKeyConstraintFrom(Cursor columnsCursor) {
        if (isTableInfoItemAnIntegerPrimaryKey(columnsCursor)) {
            String columnName = columnsCursor.getString(columnsCursor.getColumnIndex(COLUMN_NAME));
            return new Constraint(Collections.singletonList(columnName));
        }
        return null;
    }

    private static boolean isTableInfoItemAnIntegerPrimaryKey(Cursor cursor) {
        int primaryKeyIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_PRIMARY_KEY_INDEX));
        String columnType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

        // NOT A BOOLEAN AS INTEGER: 0 if not part of PK, otherwise index of column in key: https://www.sqlite.org/pragma.html#pragma_table_info
        boolean isPrimaryKey = primaryKeyIndex != 0;
        boolean isInteger = SQLiteType.fromName(columnType) == SQLiteType.INTEGER;

        return isPrimaryKey && isInteger;
    }

    private Cursor queryIndexListFor(String table) {
        return database.rawQuery(String.format(PRAGMA_INDEX_LIST, table), null);
    }

    private List<Constraint> parseUniqueConstraintsFrom(Cursor indexCursor) {
        List<Constraint> constraints = new ArrayList<>();
        while (indexCursor.moveToNext()) {
            Constraint constraint = uniqueIndexConstraintFrom(indexCursor);
            if (constraint != null) {
                constraints.add(constraint);
            }
        }
        return constraints;
    }

    private Constraint uniqueIndexConstraintFrom(Cursor indexCursor) {
        if (!isIndexUnique(indexCursor)) {
            return null;
        }
        String indexName = indexCursor.getString(1);
        Cursor indexInfoCursor = queryInfoFor(indexName);
        List<String> columns = parseIndexColumnsFrom(indexInfoCursor);
        indexInfoCursor.close();
        return new Constraint(columns);
    }

    private boolean isIndexUnique(Cursor indexCursor) {
        return indexCursor.getInt(2) == 1;
    }

    private Cursor queryInfoFor(String index) {
        return database.rawQuery(String.format(PRAGMA_INDEX_INFO, index), null);
    }

    private List<String> parseIndexColumnsFrom(Cursor indexInfoCursor) {
        List<String> columns = new ArrayList<>(indexInfoCursor.getCount());
        while (indexInfoCursor.moveToNext()) {
            String columnName = indexInfoCursor.getString(2);
            columns.add(columnName);
        }
        return columns;
    }

}

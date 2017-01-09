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

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";
    private static final String PRAGMA_TABLE_INFO = "PRAGMA table_info('%1$s');";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";
    private static final String COLUMN_PRIMARY_KEY_INDEX = "pk";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TYPE = "type";

    private static final List<String> DEFAULT_TABLES = Collections.singletonList("android_metadata");

    private final SQLiteDatabase database;

    private List<String> createdTables;
    private Map<String, List<String>> foreignTables = new TreeMap<>();
    private Map<String, List<Constraint>> uniqueConstraints = new TreeMap<>();

    public DatabaseStructure(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> tables() {
        final Cursor tablesCursor = database.rawQuery(SELECT_TABLES_NAME, null);
        createdTables = new ArrayList<>(tablesCursor.getCount());
        parseTables(tablesCursor);
        tablesCursor.close();
        return Collections.unmodifiableList(createdTables);
    }

    private void parseTables(Cursor tablesCursor) {
        while (tablesCursor.moveToNext()) {
            addCreatedTableIfNotDefault(tablesCursor.getString(0));
        }
    }

    private void addCreatedTableIfNotDefault(String tableName) {
        if (!DEFAULT_TABLES.contains(tableName)) {
            createdTables.add(tableName);
        }
    }

    public Map<String, String> projectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new TreeMap<>();
        projection.put("_id", parent + "._id AS _id");
        projection.putAll(projectionFor(parent, columns(parent)));
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

    private Map<String, String> projectionFor(String... foreignTables) {
        Map<String, String> projection = new TreeMap<>();
        for (String ft : foreignTables) {
            projection.putAll(projectionFor(ft, columns(ft)));
        }
        return projection;
    }

    public List<String> foreignTables(String table) {
        foreignTables.put(table, new ArrayList<String>(5));
        final Cursor columnsCursor = queryTableColumnsFor(table);
        List<String> allTables = tables();
        parseForeignTablesFor(table, columnsCursor, allTables);
        columnsCursor.close();
        return Collections.unmodifiableList(foreignTables.get(table));
    }

    private void parseForeignTablesFor(String table, Cursor columnsCursor, List<String> allTables) {
        while (columnsCursor.moveToNext()) {
            String columnName = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_NAME));
            addForeignTableIfExists(table, columnName, allTables);
        }
    }

    private void addForeignTableIfExists(String forTable, String columnName, List<String> tables) {
        if (!isForeignKey(columnName)) {
            return;
        }
        String tableName = columnName.substring(0, columnName.lastIndexOf('_'));
        if (tables.contains(tableName + "s")) {
            foreignTables.get(forTable).add(tableName + "s");
        } else if (tables.contains(tableName)) {
            foreignTables.get(forTable).add(tableName);
        }
    }

    private boolean isForeignKey(String columnName) {
        return columnName.endsWith("_id");
    }

    public Map<String, SQLiteType> columns(String table) {
        final Cursor columnsCursor = queryTableColumnsFor(table);
        Map<String, SQLiteType> columns = parseColumns(columnsCursor);
        columnsCursor.close();
        return Collections.unmodifiableMap(columns);
    }

    private Map<String, SQLiteType> parseColumns(Cursor columnsCursor) {
        Map<String, SQLiteType> columns = new HashMap<>(columnsCursor.getCount());
        while (columnsCursor.moveToNext()) {
            String name = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_NAME));
            String type = columnsCursor.getString(columnsCursor.getColumnIndexOrThrow(COLUMN_TYPE));
            columns.put(name, SQLiteType.fromName(type));
        }
        return columns;
    }

    private Cursor queryTableColumnsFor(String table) {
        return database.rawQuery(String.format(PRAGMA_TABLE_INFO, table), null);
    }

    public List<Constraint> uniqueConstraints(String table) {
        uniqueConstraints.put(table, new ArrayList<Constraint>());
        lookForImplicitUniqueIndex(table);
        final Cursor indexCursor = queryIndexListForTable(table);
        parseUniqueConstraints(indexCursor, table);
        indexCursor.close();
        return Collections.unmodifiableList(uniqueConstraints.get(table));
    }

    private void lookForImplicitUniqueIndex(String table) {
        // This is an implicit unique index, and won't show up querying the other indexes
        Constraint integerPrimaryKeyConstraint = findIntegerPrimaryKeyConstraint(table);
        if (integerPrimaryKeyConstraint != null) {
            uniqueConstraints.get(table).add(integerPrimaryKeyConstraint);
        }
    }

    private void parseUniqueConstraints(Cursor indexCursor, String forTable) {
        while (indexCursor.moveToNext()) {
            addUniqueIndexConstraint(indexCursor, forTable);
        }
    }

    private void addUniqueIndexConstraint(Cursor indexCursor, String forTable) {
        if (!isIndexUnique(indexCursor)) {
            return;
        }
        String indexName = indexCursor.getString(1);
        final Cursor indexInfoCursor = queryIndexInfo(indexName);
        List<String> columns = parseIndexColumns(indexInfoCursor);
        indexInfoCursor.close();
        uniqueConstraints.get(forTable).add(new Constraint(columns));
    }

    private boolean isIndexUnique(Cursor indexCursor) {
        return indexCursor.getInt(2) == 1;
    }

    private List<String> parseIndexColumns(Cursor indexInfoCursor) {
        List<String> columns = new ArrayList<>(indexInfoCursor.getCount());
        while (indexInfoCursor.moveToNext()) {
            String columnName = indexInfoCursor.getString(2);
            columns.add(columnName);
        }
        return columns;
    }

    private Constraint findIntegerPrimaryKeyConstraint(String table) {
        final Cursor columnsCursor = queryTableColumnsFor(table);
        try {
            return parseColumnsForIntegerPrimaryKeyConstraint(columnsCursor);
        } finally {
            columnsCursor.close();
        }
    }

    private Constraint parseColumnsForIntegerPrimaryKeyConstraint(Cursor columnsCursor) {
        Constraint integerPrimaryKeyConstraint = null;
        while (columnsCursor.moveToNext() && integerPrimaryKeyConstraint == null) {
            integerPrimaryKeyConstraint = extractIntegerPrimaryKeyConstraintFromColumn(columnsCursor);
        }
        return integerPrimaryKeyConstraint;
    }

    private Constraint extractIntegerPrimaryKeyConstraintFromColumn(Cursor columnsCursor) {
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

    private Cursor queryIndexListForTable(String table) {
        return database.rawQuery(String.format(PRAGMA_INDEX_LIST, table), null);
    }

    private Cursor queryIndexInfo(String index) {
        return database.rawQuery(String.format(PRAGMA_INDEX_INFO, index), null);
    }

}

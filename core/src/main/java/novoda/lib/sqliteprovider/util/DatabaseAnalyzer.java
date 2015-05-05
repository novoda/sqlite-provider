
package novoda.lib.sqliteprovider.util;

import android.content.ContentValues;
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
import novoda.rest.database.SQLiteTableCreator;

public final class DatabaseAnalyzer {

    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";
    private static final String PRAGMA_TABLE = "PRAGMA table_info(\"%1$s\");";
    private static final String PRAGMA_INDEX_LIST = "PRAGMA index_list('%1$s');";
    private static final String PRAGMA_INDEX_INFO = "PRAGMA index_info('%1$s');";

    private static List<String> defaultTables = Arrays.asList("android_metadata");

    private final SQLiteDatabase database;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> getForeignTables(String table) {
        final Cursor cur = executeQuery(String.format(PRAGMA_TABLE, table));
        List<String> tables = getTables();
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
    public List<String> getTables() {
        final Cursor cur = executeQuery(SELECT_TABLES_NAME);
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

    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new HashMap<String, String>();
        projection.put("_id", parent + "._id AS _id");
        for (Entry<String, SQLiteType> entry : getColumns(parent).entrySet()) {
            projection.put(parent + "_" + entry.getKey(), parent + "." + entry.getKey() + " AS "
                    + parent + "_" + entry.getKey());
        }

        for (String ft : foreignTables) {
            for (Entry<String, SQLiteType> entry : getColumns(ft).entrySet()) {
                projection.put(ft + "_" + entry.getKey(), ft + "." + entry.getKey() + " AS " + ft
                        + "_" + entry.getKey());
            }
        }
        return Collections.unmodifiableMap(projection);
    }

    public Map<String, SQLiteType> getColumns(String table) {
        final Cursor cur = executeQuery(String.format(PRAGMA_TABLE, table));
        Map<String, SQLiteType> fields = new HashMap<String, SQLiteType>(cur.getCount());

        while (cur.moveToNext()) {
            String name = cur.getString(cur.getColumnIndexOrThrow("name"));
            String type = cur.getString(cur.getColumnIndexOrThrow("type"));
            fields.put(name, SQLiteType.valueOf(type.toUpperCase()));
        }
        cur.close();
        return Collections.unmodifiableMap(fields);
    }

    private Cursor executeQuery(String query) {
        return database.rawQuery(query, null);
    }

    /**
     * Gets the version of SQLite used by Android.
     *
     * @return the SQLite version
     */
    public String getSQLiteVersion() {
        final Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null).rawQuery(
                "select sqlite_version() AS sqlite_version", null);
        StringBuilder sqliteVersion = new StringBuilder();
        while (cursor.moveToNext()) {
            sqliteVersion.append(cursor.getString(0));
        }
        cursor.close();
        return sqliteVersion.toString();
    }

    public List<Constraint> getUniqueConstraints(SQLiteDatabase db, String table) {
        List<Constraint> constraints = new ArrayList<Constraint>();
        final Cursor indexCursor = db.rawQuery(String.format(PRAGMA_INDEX_LIST, table), null);
        while (indexCursor.moveToNext()) {
            int isUnique = indexCursor.getInt(2);
            if (isUnique == 1) {
                String indexName = indexCursor.getString(1);
                final Cursor columnCursor = db.rawQuery(String.format(PRAGMA_INDEX_INFO, indexName), null);
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

    public String contentValuestoTableCreate(ContentValues values, String table) {
        StringBuilder string = new StringBuilder("CREATE TABLE ")
                .append(table)
                .append(" (");

        for (Entry<String, Object> entry : values.valueSet()) {
            string.append(entry.getKey())
                    .append(" TEXT, ");
        }

        return string.delete(string.length() - 2, string.length())
                .append(");")
                .toString();
    }

    public String getCreateStatement(SQLiteTableCreator creator) {

        String primaryKeyColumnName = creator.getPrimaryKey();
        novoda.rest.database.SQLiteType primaryKeyColumnType;
        boolean shouldAutoincrement;
        if (primaryKeyColumnName == null) {
            primaryKeyColumnName = "_id";
            primaryKeyColumnType = novoda.rest.database.SQLiteType.INTEGER;
            shouldAutoincrement = true;
        } else {
            primaryKeyColumnType = creator.getType(primaryKeyColumnName);
            shouldAutoincrement = creator.shouldPKAutoIncrement();
        }


        StringBuilder sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                .append("\"").append(creator.getTableName()).append("\"")
                .append(" (").append(primaryKeyColumnName).append(" ").append(primaryKeyColumnType.name())
                .append(" PRIMARY KEY ");

        if (shouldAutoincrement) {
            sql.append("AUTOINCREMENT ");
        }

        for (String columnName : creator.getTableFields()) {
            if (columnName.equals(primaryKeyColumnName)) {
                continue;
            }
            sql.append(", ").append(columnName).append(" ").append(creator.getType(columnName).name());

            if (!creator.isNullAllowed(columnName)) {
                sql.append(" NOT NULL");
            }

            if (creator.isUnique(columnName)) {
                sql.append(" UNIQUE");
            }

            if (creator.onConflict(columnName) != null && creator.isUnique(columnName)) {
                sql.append(" ON CONFLICT ").append(creator.onConflict(columnName));
            }
        }

        sql.append(");");
        return sql.toString();
    }
}

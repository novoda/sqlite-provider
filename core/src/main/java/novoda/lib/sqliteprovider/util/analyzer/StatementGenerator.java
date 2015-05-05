package novoda.lib.sqliteprovider.util.analyzer;

import android.content.ContentValues;

import java.util.Map;

import novoda.rest.database.SQLiteTableCreator;

public class StatementGenerator {

    public String contentValuestoTableCreate(ContentValues values, String table) {
        StringBuilder string = new StringBuilder("CREATE TABLE ")
                .append(table)
                .append(" (");

        for (Map.Entry<String, Object> entry : values.valueSet()) {
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

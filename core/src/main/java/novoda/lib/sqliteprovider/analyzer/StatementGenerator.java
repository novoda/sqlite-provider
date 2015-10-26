package novoda.lib.sqliteprovider.analyzer;

import android.content.ContentValues;

import java.util.Map;

import novoda.rest.database.SQLiteTableCreator;
import novoda.rest.database.SQLiteType;

public class StatementGenerator {

    public String contentValuestoTableCreate(ContentValues values, String tableName) {
        StringBuilder statement = new StringBuilder("CREATE TABLE ")
                .append(tableName)
                .append(" (");

        for (Map.Entry<String, Object> entry : values.valueSet()) {
            statement.append(entry.getKey())
                    .append(" TEXT, ");
        }

        return statement.delete(statement.length() - 2, statement.length())
                .append(");")
                .toString();
    }

    public String getCreateStatement(SQLiteTableCreator creator) {

        String primaryKeyColumnName = creator.getPrimaryKey();
        SQLiteType primaryKeyColumnType;
        boolean shouldAutoincrement;
        if (primaryKeyColumnName == null) {
            primaryKeyColumnName = "_id";
            primaryKeyColumnType = SQLiteType.INTEGER;
            shouldAutoincrement = true;
        } else {
            primaryKeyColumnType = creator.getType(primaryKeyColumnName);
            shouldAutoincrement = creator.shouldPKAutoIncrement();
        }


        StringBuilder statement = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                .append("\"").append(creator.getTableName()).append("\"")
                .append(" (").append(primaryKeyColumnName).append(" ").append(primaryKeyColumnType.name())
                .append(" PRIMARY KEY ");

        if (shouldAutoincrement) {
            statement.append("AUTOINCREMENT ");
        }

        for (String columnName : creator.getTableFields()) {
            if (columnName.equals(primaryKeyColumnName)) {
                continue;
            }
            SQLiteType columnType = creator.getType(columnName);
            statement.append(", ").append(columnName).append(" ").append(columnType.name());

            if (!creator.isNullAllowed(columnName)) {
                statement.append(" NOT NULL");
            }

            if (creator.isUnique(columnName)) {
                statement.append(" UNIQUE");
            }

            if (creator.onConflict(columnName) != null && creator.isUnique(columnName)) {
                statement.append(" ON CONFLICT ").append(creator.onConflict(columnName));
            }
        }

        return statement.append(");")
                .toString();
    }
}

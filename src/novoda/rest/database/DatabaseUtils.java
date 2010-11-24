
package novoda.rest.database;

import novoda.rest.net.ETag;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;

import java.util.Map.Entry;

public class DatabaseUtils {

    private static String table = "call_status";

    private static String query_column = "uri";

    private static String etag_column = "etag";

    private static String last_modified_column = "lastModified";

    public static ETag etagForQuery(SQLiteDatabase db, String query) {
        Parcel p = Parcel.obtain();
        Cursor cursor = db.query(table, new String[] {
                etag_column, last_modified_column
        }, new StringBuilder(query_column).append("=?").toString(), new String[] {
            query
        }, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                p.writeString(cursor.getString(0));
                p.writeString(cursor.getString(1));
            }
            return ETag.CREATOR.createFromParcel(p);
        } finally {
            p.recycle();
            cursor.close();
        }
    }

    public static String contentValuestoTableCreate(ContentValues values, String table) {
        StringBuffer buf = new StringBuffer("CREATE TABLE ").append(table).append(" (");
        for (Entry<String, Object> entry : values.valueSet()) {
            buf.append(entry.getKey()).append(" TEXT").append(", ");
        }
        buf.delete(buf.length() - 2, buf.length());
        buf.append(");");
        return buf.toString();
    }

    public static String getCreateStatement(SQLiteTableCreator creator) {
        String primaryKey = creator.getPrimaryKey();
        SQLiteType primaryKeyType;
        boolean shouldAutoincrement;
        if (primaryKey == null) {
            primaryKey = "_id";
            primaryKeyType = SQLiteType.INTEGER;
            shouldAutoincrement = true;
        } else {
            primaryKeyType = creator.getType(primaryKey);
            shouldAutoincrement = creator.shouldPKAutoIncrement();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(creator.getTableName()).append(" (")
                .append(primaryKey).append(" ").append(primaryKeyType.name())
                .append(" PRIMARY KEY").append(((shouldAutoincrement) ? " AUTOINCREMENT " : " "));

        for (String f : creator.getTableFields()) {
            if (f.equals(primaryKey)) {
                continue;
            }
            sql.append(", ").append(f).append(" ").append(creator.getType(f).name());
            sql.append(creator.isNullAllowed(f) ? "" : " NOT NULL");

            sql.append(creator.isUnique(f) ? " UNIQUE" : "");
            sql.append((creator.onConflict(f) != null && creator.isUnique(f)) ? " ON CONFLICT "
                    + creator.onConflict(f) : "");
        }

        sql.append(");");
        return sql.toString();
    }
}

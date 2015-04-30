
package novoda.lib.sqliteprovider.provider.action;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import java.util.Arrays;
import java.util.List;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;
import novoda.lib.sqliteprovider.util.Constraint;
import novoda.lib.sqliteprovider.util.Log;
import novoda.lib.sqliteprovider.util.UriUtils;

/**
 * Ability to do an Upsert rather then insert/replace as we loose the
 * relationship with the foreign keys.
 *
 * @author acsia
 */
public class InsertHelper {

    private final ExtendedSQLiteOpenHelper dbHelper;

    public InsertHelper(ExtendedSQLiteOpenHelper helper) {
        this.dbHelper = helper;
    }

    public long insert(Uri uri, ContentValues values) {
        ContentValues insertValues = (values != null) ? new ContentValues(values) : new ContentValues();
        final String table = UriUtils.getItemDirID(uri);
        final Constraint constraint = dbHelper.getFirstConstraint(table, insertValues);
        appendParentReference(uri, insertValues);
        long rowId = -1;
        if (constraint != null) {
            rowId = tryUpdateWithConstraint(table, constraint, insertValues);
        } else {
            if (Log.Provider.warningLoggingEnabled()) {
                Log.Provider.w("No constrain against URI: " + uri);
            }
        }
        if (rowId <= 0) {
            rowId = dbHelper.getWritableDatabase().insert(table, null, insertValues);
        }
        // According to http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String,%20java.lang.String,%20android.content.ContentValues)
        // only -1 indicates an error, and returning 0 is valid and has been seen.
        if (rowId != -1) {
            return rowId;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * Use {@link #tryUpdateWithConstraint(String, Constraint, ContentValues)}
     */
    @Deprecated
    protected long tryUpdateWithConstrain(String table, String constrain, ContentValues values) {
        long rowId = -1;
        int update = dbHelper.getWritableDatabase().update(table, values, constrain + "=?",
                new String[]{
                        values.getAsString(constrain)
                });

        if (Log.Provider.verboseLoggingEnabled()) {
            Log.Provider.v("Constrain " + constrain + " yield " + update);
        }
        if (update > 0) {
            rowId = getRowIdForUpdate(table, new Constraint(Arrays.asList(constrain)), values);
        }
        return rowId;
    }

    protected long tryUpdateWithConstraint(String table, Constraint constraint, ContentValues values) {
        long rowId = -1;
        String whereClause = getWhereClause(constraint);
        String[] whereArgs = getWhereArguments(constraint, values);
        int update = dbHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);

        if (Log.Provider.verboseLoggingEnabled()) {
            Log.Provider.v("Constrain " + constraint + " yield " + update);
        }
        if (update > 0) {
            rowId = getRowIdForUpdate(table, constraint, values);
        }
        return rowId;
    }

    private String getWhereClause(Constraint constraint) {
        List<String> columns = constraint.getColumns();
        String whereClause = "";
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            if (i > 0) {
                whereClause += " AND ";
            }
            whereClause += column + "=?";
        }
        return whereClause;
    }

    private String[] getWhereArguments(Constraint constraint, ContentValues values) {
        List<String> columns = constraint.getColumns();
        int columnCount = columns.size();
        String[] whereArgs = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            String column = columns.get(i);
            whereArgs[i] = values.getAsString(column);
        }
        return whereArgs;
    }

    /**
     * Will get the Row id from the latest update.
     *
     * @param table
     * @param constraint
     * @param values
     * @return
     */
    private long getRowIdForUpdate(String table, Constraint constraint, ContentValues values) {
        final Cursor cur = dbHelper.getReadableDatabase().query(table, new String[]{
                "rowid"
        }, getWhereClause(constraint), getWhereArguments(constraint, values), null, null, null);
        if (!cur.moveToFirst()) {
            return -1;
        }
        try {
            return cur.getLong(0);
        } finally {
            cur.close();
        }
    }

    protected void appendParentReference(Uri uri, ContentValues insertValues) {
        if (UriUtils.hasParent(uri) && !insertValues.containsKey(UriUtils.getParentId(uri) + "_id")) {
            insertValues.put(UriUtils.getParentColumnName(uri) + "_id", UriUtils.getParentId(uri));
        }
    }
}


package novoda.lib.sqliteprovider.provider.action;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;
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
        final String firstConstrain = dbHelper.getFirstConstrain(table, insertValues);
        appendParentReference(uri, insertValues);
        long rowId = -1;
        if (firstConstrain != null) {
            rowId = tryUpdateWithConstrain(table, firstConstrain, insertValues);
        } else {
            if (Log.Provider.warningLoggingEnabled()) {
                Log.Provider.w("No constrain against URI: " + uri);
            }
        }
        if (rowId < 0) {
            rowId = dbHelper.getWritableDatabase().insert(table, null, insertValues);
        }
        if (rowId > 0) {
            return rowId;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    private long tryUpdateWithConstrain(String table, String constrain, ContentValues values) {
        long rowId = -1;
        int update = dbHelper.getWritableDatabase().update(table, values, constrain + "=?",
                new String[] {
                values.getAsString(constrain)
        });

        if (Log.Provider.verboseLoggingEnabled()) {
            Log.Provider.v("Constrain " + constrain + " yield " + update);
        }
        if (update > 0) {
            rowId = getRowIdForUpdate(table, constrain, values);
        }
        return rowId;
    }

    /**
     * Will get the Row id from the latest update.
     *
     * @param table
     * @param constrain
     * @param values
     * @return
     */
    private long getRowIdForUpdate(String table, String constrain, ContentValues values) {
        final Cursor cur = dbHelper.getReadableDatabase().query(table, new String[] {
                "_id"
        }, constrain + "=?", new String[] {
                values.getAsString(constrain)
        }, null, null, null);
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

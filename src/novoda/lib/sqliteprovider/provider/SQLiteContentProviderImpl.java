
package novoda.lib.sqliteprovider.provider;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper2;
import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteQueryBuilder;
import novoda.lib.sqliteprovider.util.Log.Provider;
import novoda.lib.sqliteprovider.util.UriUtils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SQLiteContentProviderImpl extends SQLiteContentProvider {

    private static final String ID = "_id";

    private static final String GROUP_BY = "groupBy";

    private static final String HAVING = "having";

    private static final String LIMIT = "limit";

    private static final String EXPAND = "expand";

    @Override
    protected SQLiteOpenHelper getDatabaseHelper(Context context) {
        try {
            return new ExtendedSQLiteOpenHelper2(context);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    protected Uri insertInTransaction(Uri uri, ContentValues values) {
        ContentValues insertValues = (values != null) ? new ContentValues(values)
                : new ContentValues();
        if (UriUtils.hasParent(uri)) {
            if (!insertValues.containsKey(UriUtils.getParentId(uri) + "_id")) {
                insertValues.put(UriUtils.getParentColumnName(uri) + "_id",
                        UriUtils.getParentId(uri));
            }
        }
        long rowId = getWritableDatabase().insert(UriUtils.getItemDirID(uri), null, insertValues);
        if (rowId > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, rowId);
            notifyUriChange(newUri);
            return newUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return getDatabaseHelper().getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return getDatabaseHelper().getReadableDatabase();
    }

    @Override
    protected int updateInTransaction(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        ContentValues insertValues = (values != null) ? new ContentValues(values)
                : new ContentValues();
        int rowId = getWritableDatabase().update(UriUtils.getItemDirID(uri), insertValues,
                selection, selectionArgs);
        if (rowId > 0) {
            Uri insertUri = ContentUris.withAppendedId(uri, rowId);
            notifyUriChange(insertUri);
            return rowId;
        }
        throw new SQLException("Failed to update row into " + uri);
    }

    @Override
    protected int deleteInTransaction(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = getWritableDatabase();
        int count = database.delete(UriUtils.getItemDirID(uri), selection, selectionArgs);
        notifyUriChange(uri);
        return count;
    }

    @Override
    protected void notifyChange() {
    }

    public void notifyUriChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        final ExtendedSQLiteQueryBuilder builder = getSQLiteQueryBuilder();

        final List<String> expands = uri.getQueryParameters(EXPAND);
        final String groupBy = uri.getQueryParameter(GROUP_BY);
        final String having = uri.getQueryParameter(HAVING);
        final String limit = uri.getQueryParameter(LIMIT);

        final StringBuilder tableName = new StringBuilder(UriUtils.getItemDirID(uri));
        builder.setTables(tableName.toString());
        Map<String, String> autoproj = null;

        if (expands.size() > 0) {
            builder.addInnerJoin(expands.toArray(new String[] {}));
            ExtendedSQLiteOpenHelper2 helper = (ExtendedSQLiteOpenHelper2) getDatabaseHelper();
            autoproj = helper.getProjectionMap(tableName.toString(),
                    expands.toArray(new String[] {}));
            builder.setProjectionMap(autoproj);
        }

        if (UriUtils.isItem(uri)) {
            builder.appendWhere(ID + "=" + uri.getLastPathSegment());
        } else {
            if (UriUtils.hasParent(uri)) {
                builder.appendWhereEscapeString(UriUtils.getParentColumnName(uri) + ID + "="
                        + UriUtils.getParentId(uri));
            }
        }

        if (Provider.verboseLoggingEnabled()) {
            Provider.v("Querying the following:");
            Provider.v("Uri:" + uri.toString());
            Provider.v("table:" + builder.getTables());
            Provider.v("projection:" + Arrays.toString(projection));
            Provider.v("selection:" + selection + " with arguments "
                    + Arrays.toString(selectionArgs));
            Provider.v("extra args:" + groupBy + " ,having: " + having + " ,sort order: "
                    + sortOrder + " ,limit: " + limit);
            Provider.v("projectionAutomated:" + autoproj);
        }
        return builder.query(getReadableDatabase(), projection, selection, selectionArgs, groupBy,
                having, sortOrder, limit);
    }

    private ExtendedSQLiteQueryBuilder getSQLiteQueryBuilder() {
        return new ExtendedSQLiteQueryBuilder();
    }
}

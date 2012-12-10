package novoda.lib.sqliteprovider.provider;

import android.content.*;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import novoda.lib.sqliteprovider.provider.action.InsertHelper;
import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;
import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteQueryBuilder;
import novoda.lib.sqliteprovider.util.Log;
import novoda.lib.sqliteprovider.util.UriUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SQLiteContentProviderImpl extends SQLiteContentProvider {

    protected static final String ID = "_id";
    private static final String GROUP_BY = "groupBy";
    private static final String HAVING = "having";
    private static final String LIMIT = "limit";
    private static final String EXPAND = "expand";
    private static final String DISTINCT = "distinct";

    private InsertHelper helper;
    private final ImplLogger logger;

    public SQLiteContentProviderImpl() {
        logger = new ImplLogger();
    }

    @Override
    public boolean onCreate() {
        super.onCreate();
        helper = new InsertHelper((ExtendedSQLiteOpenHelper) getDatabaseHelper());
        return true;
    }

    protected SQLiteDatabase getWritableDatabase() {
        return getDatabaseHelper().getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return getDatabaseHelper().getReadableDatabase();
    }

    @Override
    protected SQLiteOpenHelper getDatabaseHelper(Context context) {
        try {
            return new ExtendedSQLiteOpenHelper(context);
        } catch (IOException e) {
            Log.Provider.e(e);
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    protected Uri insertInTransaction(Uri uri, ContentValues values) {
        long rowId = helper.insert(uri, values);
        if (rowId > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, rowId);
            notifyUriChange(newUri);
            return newUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    protected int updateInTransaction(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ContentValues insertValues = (values != null) ? new ContentValues(values) : new ContentValues();

        int rowId = getWritableDatabase().update(UriUtils.getItemDirID(uri), insertValues, selection, selectionArgs);

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
        getContext().getContentResolver().notifyChange(uri, null, getNotificationSyncToNetwork());
    }

    public boolean getNotificationSyncToNetwork() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        logger.logStart(uri);

        final ExtendedSQLiteQueryBuilder builder = getSQLiteQueryBuilder();

        final List<String> expands = uri.getQueryParameters(EXPAND);
        final String groupBy = uri.getQueryParameter(GROUP_BY);
        final String having = uri.getQueryParameter(HAVING);
        final String limit = uri.getQueryParameter(LIMIT);

        builder.setDistinct("true".equals(uri.getQueryParameter(DISTINCT)));

        final StringBuilder tableName = new StringBuilder(UriUtils.getItemDirID(uri));
        builder.setTables(tableName.toString());
        Map<String, String> autoproj = null;

        if (expands.size() > 0) {
            builder.addInnerJoin(expands.toArray(new String[] {}));
            ExtendedSQLiteOpenHelper extendedHelper = (ExtendedSQLiteOpenHelper) getDatabaseHelper();
            autoproj = extendedHelper.getProjectionMap(tableName.toString(), expands.toArray(new String[] {}));
            builder.setProjectionMap(autoproj);
        }

        if (UriUtils.isItem(uri)) {
            String where = ID + "=" + uri.getLastPathSegment();
            logger.logAppendWhere(where);
            builder.appendWhere(where);
        } else if (UriUtils.hasParent(uri)) {
            StringBuilder escapedWhere = new StringBuilder();
            DatabaseUtils.appendEscapedSQLString(escapedWhere, UriUtils.getParentId(uri));
            String where = UriUtils.getParentColumnName(uri) + ID + "=" + escapedWhere.toString();
            logger.logAppendWhere(where);
            builder.appendWhere(where);
        }

        logger.logEnd(projection, selection, selectionArgs, sortOrder, builder, groupBy, having, limit, autoproj);

        Cursor cursor = builder.query(getReadableDatabase(), projection, selection, selectionArgs, groupBy, having, sortOrder, limit);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    protected ExtendedSQLiteQueryBuilder getSQLiteQueryBuilder() {
        return new ExtendedSQLiteQueryBuilder();
    }
}

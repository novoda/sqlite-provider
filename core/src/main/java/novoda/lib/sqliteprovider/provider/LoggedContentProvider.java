
package novoda.lib.sqliteprovider.provider;

import novoda.lib.sqliteprovider.cursor.EmptyCursor;
import novoda.lib.sqliteprovider.util.Log;
import novoda.lib.sqliteprovider.util.Log.ContentProvider.CPType;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class LoggedContentProvider extends ContentProvider {

    private String logTag = null;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.ContentProvider.log(CPType.DELETE, logTag, uri, selectionArgs, selection,
                selectionArgs, null, null);
        return 1;
    }

    @Override
    public String getType(Uri uri) {
        Log.ContentProvider.log(CPType.GET_TYPE, logTag, uri, null, null, null, null, null);
        return "logged/uri+" + uri;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.ContentProvider.log(CPType.INSERT, logTag, uri, null, null, null, null, values);
        return ContentUris.withAppendedId(uri, 1);
    }

    @Override
    public boolean onCreate() {
        if (getLogTag() == null) {
            setLogTag(LoggedContentProvider.class.getSimpleName());
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Log.ContentProvider.log(CPType.QUERY, logTag, uri, projection, selection, selectionArgs,
                sortOrder, null);
        return new EmptyCursor();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.ContentProvider.log(CPType.QUERY, logTag, uri, null, selection, selectionArgs, null,
                values);
        return 1;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public String getLogTag() {
        return logTag;
    }
}

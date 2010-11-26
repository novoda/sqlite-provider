package novoda.lib.sqliteprovider.provider;

import android.content.ContentProvider;
import android.net.Uri;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

public class SQLiteProvider extends ContentProvider {
	public static final Uri CONTENT_URI = Uri
			.parse("content://novoda.lib.sqliteprovider.provider.sqliteprovider");

	/**
	 * @see android.content.ContentProvider#delete(Uri,String,String[])
	 */
	@Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Put your code here
        return 0;
	}

	/**
	 * @see android.content.ContentProvider#getType(Uri)
	 */
	@Override
    public String getType(Uri uri) {
		// TODO Put your code here
        return null;
	}

	/**
	 * @see android.content.ContentProvider#insert(Uri,ContentValues)
	 */
	@Override
    public Uri insert(Uri uri, ContentValues values) {
		// TODO Put your code here
        return null;
	}

	/**
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
    public boolean onCreate() {
		// TODO Put your code here
        return false;
	}

	/**
	 * @see android.content.ContentProvider#query(Uri,String[],String,String[],String)
	 */
	@Override
    public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        return new MatrixCursor(null);
	}

	/**
	 * @see android.content.ContentProvider#update(Uri,ContentValues,String,String[])
	 */
	@Override
    public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Put your code here
        return 0;
	}
}

package novoda.rest.database.etag;

import novoda.rest.net.ETag;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ETagSQLiteHelper extends SQLiteOpenHelper {

	private static final int MAX_ROWS = 100;

	private static final String TAG = ETagSQLiteHelper.class.getSimpleName();

	private static final String TABLE_NAME = "etag";

	private static final String ETAG_TABLE_DROP = "DROP TABLE IF EXISTS "
			+ TABLE_NAME + ";";

	private static final String URL_COLUMN = "url";

	private static final String ETAG_COLUMN = "etag";

	private static final String LAST_MODIFIED_COLUMN = "lastModified";

	private static final String UPDATED_COLUMN = "updatedAt";

	private static int DB_VERSION = 3;

	private static final String ETAG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " (_id INTEGER NOT NULL PRIMARY KEY, "
			+ ETAG_COLUMN
			+ " TEXT NOT NULL UNIQUE, "
			+ URL_COLUMN
			+ " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, "
			+ LAST_MODIFIED_COLUMN
			+ " TEXT NOT NULL, "
			+ UPDATED_COLUMN
			+ " INTEGER NOT NULL);";

	public ETagSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.i("TESTE", "ise null" + (context == null));
	}

	public ETagSQLiteHelper(Context context, String name) {
		this(context, name, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, "creating table for etag");
		db.execSQL(ETAG_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, "upgrading table from " + oldVersion + " to " + newVersion);
		db.execSQL(ETAG_TABLE_DROP);
	}

	public synchronized long insertETagForUri(ETag etag, String uri) {
		final SQLiteDatabase writableDatabase = getWritableDatabase();

		long time = System.currentTimeMillis();

		final ContentValues values = new ContentValues(3);
		values.put(ETAG_COLUMN, etag.etag);
		values.put(LAST_MODIFIED_COLUMN, etag.lastModified);
		values.put(URL_COLUMN, uri);
		values.put(UPDATED_COLUMN, time);

		writableDatabase.acquireReference();
		long ret = -1;
		if (getCount() >= MAX_ROWS) {
			ret = writableDatabase.update(TABLE_NAME, values, UPDATED_COLUMN
					+ "=(select min(" + UPDATED_COLUMN + ") from " + TABLE_NAME
					+ ")", null);
		} else {
			ret = writableDatabase.insert(TABLE_NAME, "", values);
		}
		writableDatabase.releaseReference();
		writableDatabase.close();
		return ret;
	}

	public synchronized ETag getETag(String uri) {
		Cursor cur = getReadableDatabase().query(TABLE_NAME,
				new String[] { ETAG_COLUMN, LAST_MODIFIED_COLUMN },
				URL_COLUMN + "=?", new String[] { uri }, null, null, null);
		ETag etag = new ETag();
		if (cur.moveToFirst()) {
			etag.etag = cur.getString(0);
			etag.lastModified = cur.getString(1);
		}
		if (cur != null) {
			getReadableDatabase().close();
			cur.close();
		}
		return etag;
	}

	public synchronized boolean hasEtag(String uri) {
		return false;
	}

	/* package */synchronized void clear() {
		getWritableDatabase().delete(TABLE_NAME, null, null);
	}

	private synchronized long getCount() {
		return DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_NAME);
	}
}

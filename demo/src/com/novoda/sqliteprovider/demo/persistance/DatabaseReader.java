package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DatabaseReader {

	private final ContentResolver contentResolver;

	public DatabaseReader(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public Cursor getAllFromFireworksTable() {
		return getAllFrom(TBL_FIREWORKS);
	}
	
	public Cursor getAllFrom(String tableName) {
		return contentResolver.query(Uri.parse("content://com.novoda.demo/"+ tableName), null, null, null, null);
	}

}

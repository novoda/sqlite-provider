package com.novoda.sqliteprovider.demo.persistance;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DatabaseReader {

	private final ContentResolver contentResolver;

	public DatabaseReader(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public Cursor getAllFromFireworksTable() {
		return getAllFrom("fireworks");
	}
	
	public Cursor getAllFrom(String tableName) {
		return contentResolver.query(Uri.parse("content://com.novoda.demo/"+ tableName), null, null, null, null);
	}

}

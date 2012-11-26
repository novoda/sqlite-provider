package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DatabaseReader {

	private final ContentResolver contentResolver;

	public DatabaseReader(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}
	
	/**
	 * (1) Read table example
	 */
	protected Cursor getAllFrom(String tableName) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName), null, null, null, null);
	}

}

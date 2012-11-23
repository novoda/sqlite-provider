package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;
import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

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
	
	private Cursor getAllFrom(String tableName) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName), null, null, null, null);
	}

}

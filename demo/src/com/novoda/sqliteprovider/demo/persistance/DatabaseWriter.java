package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.novoda.sqliteprovider.demo.provider.FireworkProvider;

public class DatabaseWriter {

	private final ContentResolver contentResolver;

	public DatabaseWriter(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public void saveDataToFireworksTable(ContentValues values){
		saveDataToTable(TBL_FIREWORKS, values);
	}
	
	private void saveDataToTable(String table, ContentValues values){
		contentResolver.insert(Uri.parse(FireworkProvider.AUTHORITY + table), values);
	}
	
}
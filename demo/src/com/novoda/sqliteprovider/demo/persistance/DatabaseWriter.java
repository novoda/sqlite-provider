package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;
import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;


public class DatabaseWriter {

	private final ContentResolver contentResolver;
	private final UriListener uriListener;

	public DatabaseWriter(ContentResolver contentResolver, UriListener uriListener) {
		this.contentResolver = contentResolver;
		this.uriListener = uriListener;
	}

	public void saveDataToFireworksTable(ContentValues values){
		saveDataToTable(TBL_FIREWORKS, values);
	}
	
	private void saveDataToTable(String table, ContentValues values){
		Uri uri = createUri(table);
		contentResolver.insert(uri, values);
	}
	
	private Uri createUri(String tableName) {
		Uri uri = Uri.parse(AUTHORITY + tableName);
		informListeners(uri);
		return uri;
	}

	private void informListeners(Uri uri) {
		if(uriListener != null){
			uriListener.onUriSet(uri);
		}
	}
}
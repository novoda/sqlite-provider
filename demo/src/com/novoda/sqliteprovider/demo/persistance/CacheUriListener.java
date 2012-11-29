package com.novoda.sqliteprovider.demo.persistance;

import android.net.Uri;

public class CacheUriListener implements UriListener {

	private static Uri lastUri;

	@Override
	public void onUriSet(Uri uri) {
		lastUri = uri;
	}
	
	public Uri getLastUriCalled(){
		return lastUri;
	}
}
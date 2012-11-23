package com.novoda.sqliteprovider.demo.provider;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FireworkProvider extends SQLiteContentProviderImpl {

	public static final String AUTHORITY = "content://com.novoda.demo/";

	@Override
	public boolean onCreate() {
		return super.onCreate();
	}
	
}

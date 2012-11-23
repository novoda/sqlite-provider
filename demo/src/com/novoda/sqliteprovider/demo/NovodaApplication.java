package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;

public class NovodaApplication extends Application {

	private DatabaseReader databaseReader;
	
	public DatabaseReader getDatabaseReader() {
		if(databaseReader == null){
			databaseReader = new DatabaseReader(getContentResolver());
		}
		return databaseReader;
	}
}

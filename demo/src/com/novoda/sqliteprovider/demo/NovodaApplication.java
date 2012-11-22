package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.DatabaseSetup;
import com.novoda.sqliteprovider.demo.util.Log;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class NovodaApplication extends Application {

	private static final String DB_NAME = "fireworks.db";
	private static final int DB_VERSION = 1;
	private ExtendedSQLiteOpenHelper helper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		deleteDatabase(DB_NAME);
		
		DatabaseSetup databaseSetup = new DatabaseSetup(getSqliteHelper());
		databaseSetup.createTables();
		databaseSetup.addStaticData();
	}

	public ExtendedSQLiteOpenHelper getSqliteHelper() {
		if(helper == null){
			Log.i("Helper started for: "+DB_NAME);
			helper = new ExtendedSQLiteOpenHelper(this, DB_NAME, null, DB_VERSION);
		}
		return helper;
	}
}

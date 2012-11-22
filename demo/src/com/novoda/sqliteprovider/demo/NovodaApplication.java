package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;
import com.novoda.sqliteprovider.demo.persistance.DatabaseSetup;
import com.novoda.sqliteprovider.demo.util.Log;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class NovodaApplication extends Application {

	private static final String DB_NAME = "fireworks.db";
	private static final int DB_VERSION = 1;
	private DatabaseReader databaseHelper;
	private ExtendedSQLiteOpenHelper sqLiteOpenHelper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		deleteDatabase(DB_NAME);
		
		Log.i("Helper started for: "+DB_NAME);
		sqLiteOpenHelper = new ExtendedSQLiteOpenHelper(this, DB_NAME, null, DB_VERSION);
		
		DatabaseSetup databaseSetup = new DatabaseSetup(sqLiteOpenHelper);
		databaseSetup.createTables();
		databaseSetup.addStaticData();
	}

	public DatabaseReader getDatabaseReader() {
		if(databaseHelper == null){
			databaseHelper = new DatabaseReader(sqLiteOpenHelper);
		}
		return databaseHelper;
	}
}

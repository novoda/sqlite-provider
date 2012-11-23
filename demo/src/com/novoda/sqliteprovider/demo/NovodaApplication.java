package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;
import com.novoda.sqliteprovider.demo.util.Log;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

import java.io.IOException;

public class NovodaApplication extends Application {

	private DatabaseReader databaseHelper;
	private ExtendedSQLiteOpenHelper sqLiteOpenHelper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		deleteDatabase(getPackageName()+".db");
		
		try {
			sqLiteOpenHelper = new ExtendedSQLiteOpenHelper(this);
		
//			DatabaseSetup databaseSetup = new DatabaseSetup(sqLiteOpenHelper);
//			databaseSetup.createTables();
//			databaseSetup.addStaticData();
		} catch (IOException e) {
			Log.e("Fatal database creation error", e);
		}
	}

	public DatabaseReader getDatabaseReader() {
		if(databaseHelper == null){
			databaseHelper = new DatabaseReader(sqLiteOpenHelper);
		}
		return databaseHelper;
	}
}

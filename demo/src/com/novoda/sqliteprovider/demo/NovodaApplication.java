package com.novoda.sqliteprovider.demo;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class NovodaApplication extends Application {

	private DatabaseReader databaseHelper;
	private SQLiteOpenHelper sqLiteOpenHelper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		String databaseName = getPackageName()+".db";
		deleteDatabase(databaseName);
		
//		sqLiteOpenHelper = new ExtendedSQLiteOpenHelper(this, databaseName, null, 1);
//		try {
//			sqLiteOpenHelper = new ExtendedSQLiteOpenHelper(this);
//		} catch (IOException e) {
//			Log.e("Database error", e);
//		}
//		SQLiteDatabase writableDatabase = sqLiteOpenHelper.getWritableDatabase();
		
//			DatabaseSetup databaseSetup = new DatabaseSetup(sqLiteOpenHelper);
//			databaseSetup.createTables();
//			databaseSetup.addStaticData();
	}

	public DatabaseReader getDatabaseReader() {
		if(databaseHelper == null){
			databaseHelper = new DatabaseReader((ExtendedSQLiteOpenHelper) sqLiteOpenHelper);
		}
		return databaseHelper;
	}
}

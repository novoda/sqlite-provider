package com.novoda.sqliteprovider.demo.persistance;

import android.database.sqlite.SQLiteDatabase;

import com.novoda.sqliteprovider.demo.util.Log;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class DatabaseSetup {

	private final ExtendedSQLiteOpenHelper helper;

	public DatabaseSetup(ExtendedSQLiteOpenHelper helper) {
		this.helper = helper;
	}
	
	public void createTables() {
		Log.i("Creating tables");
		
		SQLiteDatabase database = helper.getWritableDatabase();
		
		database.execSQL(SqlFireworks.CREATE_TABLE);
		Log.i("Created Fireworks table");

		
		Log.i("Created all tables");
	}

}

package com.novoda.sqliteprovider.demo.persistance;

import android.database.sqlite.SQLiteDatabase;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class DatabaseSetup {

	private final ExtendedSQLiteOpenHelper helper;

	public DatabaseSetup(ExtendedSQLiteOpenHelper helper) {
		this.helper = helper;
	}
	
	public void createTables() {
		SQLiteDatabase database = helper.getWritableDatabase();
		
		database.execSQL(SqlFireworks.CREATE_TABLE);
	}

}

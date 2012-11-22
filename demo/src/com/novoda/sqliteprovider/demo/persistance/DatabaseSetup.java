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

	public void addStaticData() {
		Log.i("Adding static data");
		SQLiteDatabase database = helper.getWritableDatabase();
		
		addFireworks(database);
	}

	private void addFireworks(SQLiteDatabase database) {
		Log.i("Adding fireworks");
		
		addFirework(database, "The Big Boy", "Red", "Rocket", "Bang");
		
	}

	private void addFirework(SQLiteDatabase database, String... args) {
		database.rawQuery(SqlFireworks.INSERT_INTO, args);
	}

}

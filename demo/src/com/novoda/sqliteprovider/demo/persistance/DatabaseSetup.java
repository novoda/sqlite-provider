package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.SqlFireworks.*;

import android.content.ContentValues;
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
		
		database.execSQL(CREATE_TABLE);
		Log.i("Created Fireworks table");

		
		Log.i("Created all tables");
	}

	public void addStaticData() {
		Log.i("Adding static data");
		SQLiteDatabase database = helper.getWritableDatabase();
		
		addFireworks(database);
		
		Log.i("Finished adding static data");
	}

	private void addFireworks(SQLiteDatabase database) {
		Log.i("Adding fireworks");
		
		addFirework(database, "The Big Boy", "Red", "Rocket", "Bang");
		addFirework(database, "Water Falls", "Blue", "Fountain", "Fizz");
		addFirework(database, "Rotar Motor", "Green", "Cathrine Wheel", "Whizz");
		addFirework(database, "The Dieing Ghost", "White", "Screamer", "Eeek");
		addFirework(database, "Asda Firework 1", "Brown", "Rocket", "Pop");
		addFirework(database, "Asda Firework 2", "Green", "Rocket", "Bang");
		addFirework(database, "Asda Firework 3", "Green and Brown", "Fountain", "Fizz");
		
		Log.i("Finished adding fireworks");
	}

	private void addFirework(SQLiteDatabase database, String... args) {
		ContentValues cv = new ContentValues();
		cv.put(COL_NAME, args[0]);
		cv.put(COL_COLOR, args[1]);
		cv.put(COL_TYPE, args[2]);
		cv.put(COL_NOISE, args[3]);
		database.insert(TBL_NAME, null, cv);
	}

}

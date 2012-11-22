package com.novoda.sqliteprovider.demo;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.novoda.sqliteprovider.demo.persistance.DatabaseSetup;
import com.novoda.sqliteprovider.demo.util.Log;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class NovodaApplication extends Application {

	private static final String DB_NAME = "fireworks.db";
	private static final int DB_VERSION = 1;
	
	@Override
	public void onCreate() {
		super.onCreate();
		ExtendedSQLiteOpenHelper helper = new ExtendedSQLiteOpenHelper(this, DB_NAME, new MyCusorFactory(), DB_VERSION);
		Log.i("Helper started for: "+DB_NAME);
		DatabaseSetup databaseSetup = new DatabaseSetup(helper);
		
		databaseSetup.createTables();
	}

	class MyCusorFactory implements CursorFactory {

		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
			return null;
		}
		
	}
}

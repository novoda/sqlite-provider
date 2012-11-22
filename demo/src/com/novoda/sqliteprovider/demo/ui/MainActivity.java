package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.view.View;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class MainActivity extends NovodaActivity {

	private static final String DB_NAME = "fireworks.db";
	private static final int DB_VERSION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ExtendedSQLiteOpenHelper helper = new ExtendedSQLiteOpenHelper(this, DB_NAME, new MyCusorFactory(), DB_VERSION);
	}
	
	public void onViewAllFireworksClick(View button){
		Intent intent = new Intent(this, ViewFireworksActivity.class);
		startActivity(intent);
	}
	
	class MyCusorFactory implements CursorFactory {

		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
			return null;
		}
		
	}
}

package com.novoda.sqliteprovider.demo.persistance;

import android.database.Cursor;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;

public class DatabaseReader {

	private final ExtendedSQLiteOpenHelper helper;

	public DatabaseReader(ExtendedSQLiteOpenHelper extendedSQLiteOpenHelper) {
		this.helper = extendedSQLiteOpenHelper;
	}

	public Cursor query(String tableName, String[] args) {
		return helper.getReadableDatabase().query(tableName, null, null, args, null, null, null);
	}

}

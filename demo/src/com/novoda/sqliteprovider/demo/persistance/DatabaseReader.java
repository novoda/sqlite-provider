package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DatabaseReader {

	private final ContentResolver contentResolver;

	public DatabaseReader(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}
	
	/**
	 * (1) Read - generic table support
	 */
	protected Cursor getAllFrom(String tableName) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName), null, null, null, null);
	}

	/**
	 * (2) Read - primary key support 
	 */
	public Cursor getFrom(String tableName, int primaryKey) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName +"/"+ primaryKey), null, null, null, null);
	}

	/**
	 * (3) Read - one to many support
	 */
	public Cursor getChildren(String parentTableName, String childTableName, int primaryKey) {
		return contentResolver.query(Uri.parse(AUTHORITY + parentTableName +"/"+ primaryKey +"/"+ childTableName), null, null, null, null);
	}

	/**
	 * (4) Read - group by & having support
	 */
	public Cursor getGroupedByAndHaving(String tableName, String column, String having) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName + "?groupBy="+column +"&having="+ having), null, null, null, null);
	}

	/**
	 * (5) Read - limit support
	 */
	public Cursor getLimited(String tableName, int limit) {
		return contentResolver.query(Uri.parse(AUTHORITY + tableName +"?limit="+ limit),  null, null, null, null);
	}

}

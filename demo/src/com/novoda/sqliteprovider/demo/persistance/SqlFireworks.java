package com.novoda.sqliteprovider.demo.persistance;

public class SqlFireworks {

	private static final String TBL_NAME = "fireworks";
	
	static final String CREATE_TABLE = "CREATE TABLE " + TBL_NAME + " (" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name" + "TEXT,"+
			"color" + "TEXT,"+
			"type" + "TEXT,"+
			"noise" + "TEXT);";

}

package com.novoda.sqliteprovider.demo.persistance;

public class SqlFireworks {

	static final String TBL_NAME = "fireworks";

	static final String COL_NOISE = "noise";
	static final String COL_TYPE = "type";
	static final String COL_COLOR = "color";
	static final String COL_NAME = "name";
	
	static final String CREATE_TABLE = "CREATE TABLE " + TBL_NAME + " (" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_NAME + " TEXT,"+
			COL_COLOR + " TEXT,"+
			COL_TYPE + " TEXT,"+
			COL_NOISE + " TEXT);";

	private SqlFireworks(){
	}
}
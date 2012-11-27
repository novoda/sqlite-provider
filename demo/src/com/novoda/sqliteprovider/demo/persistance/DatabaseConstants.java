package com.novoda.sqliteprovider.demo.persistance;

public class DatabaseConstants {

	protected static final String TBL_FIREWORKS = "firework";
	public static class Fireworks {
		protected static final String COL_NAME = "name";
		protected static final String COL_COLOR = "color";
		protected static final String COL_NOISE = "noise";
		protected static final String COL_TYPE = "ftype";
		protected static final String COL_PRICE = "price";
		protected static final String COL_SHOP = "shop_id";
		protected static final int COL_IDX_NAME = 1;
		protected static final int COL_IDX_COLOR = 2;
		protected static final int COL_IDX_TYPE = 3;
		protected static final int COL_IDX_NOISE = 4;
		protected static final int COL_IDX_PRICE = 5;
	}
	
	protected static final String TBL_SHOPS = "shop";
	public static class Shops {
		protected static final String COL_NAME = "name";
		protected static final String COL_POSTCODE = "postcode";
		protected static final int COL_IDX_NAME = 1;
		protected static final int COL_IDX_POSTCODE = 2;
		
	}
}

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
	}
	
	protected static final String TBL_SHOPS = "shop";
	public static class Shops {
		protected static final String COL_NAME = "name";
		protected static final String COL_POSTCODE = "postcode";
	}
}

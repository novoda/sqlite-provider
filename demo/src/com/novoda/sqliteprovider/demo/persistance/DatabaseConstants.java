package com.novoda.sqliteprovider.demo.persistance;

public class DatabaseConstants {

	private DatabaseConstants(){
	}
	
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

	public static class RawSql {
		public static final String SELECT_ALL = "SELECT * FROM fireworks;";
		
		public static final String INSERT_FIREWORK = "INSERT INTO fireworks " +
														"(name, color, noise, ftype, price, shop_id) " +
														"VALUES " +
														"(Na, Co, No, Ft, Pr, Sh);";

		public static final String SELECT_USING_PRIMARY_KEY = "SELECT * FROM firework WHERE (_id=1);";

		public static final String SELECT_USING_SHOP_FOREIGN_KEY = "SELECT * FROM firework WHERE (shop_id=1);";
	}
}

package com.novoda.sqliteprovider.demo.provider;

public final class FireworkUriConstants {

    private FireworkUriConstants() {
    }

    public static final String VIEW_ALL_SEARCH = FireworkProvider.AUTHORITY + "firework";
    public static final String ADD_FIREWORK = FireworkProvider.AUTHORITY + "firework (with ContentValues)";
    public static final String ADD_FIREWORK_YIELD = FireworkProvider.AUTHORITY + "firework?allowYield=true (with ContentValues)";
    public static final String ADD_FIREWORK_WITHOUT_YIELD = FireworkProvider.AUTHORITY + "firework?allowYield=false (with ContentValues)";
    public static final String PRIMARY_KEY_SEARCH = FireworkProvider.AUTHORITY + "firework/1";
    public static final String ONE_TO_MANY_SEARCH = FireworkProvider.AUTHORITY + "shop/1/firework";
    public static final String GROUP_BY_SEARCH = FireworkProvider.AUTHORITY + "firework?groupBy=shop_id&having=SUM(price)>40";
    public static final String LIMIT_3 = FireworkProvider.AUTHORITY + "firework?limit=3";
    public static final String UNIQUE_SEARCH = FireworkProvider.AUTHORITY + "firework?distinct=true";

}

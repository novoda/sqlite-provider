package com.novoda.sqliteprovider.demo.provider;

import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

public final class FireworkUriConstants {

    private FireworkUriConstants() {
    }

    public static final String VIEW_ALL_SEARCH = AUTHORITY + "firework";
    public static final String ADD_FIREWORK = AUTHORITY + "firework (with ContentValues)";
    public static final String PRIMARY_KEY_SEARCH = AUTHORITY + "firework/1";
    public static final String ONE_TO_MANY_SEARCH = AUTHORITY + "shop/1/firework";
    public static final String GROUP_BY_SEARCH = AUTHORITY + "firework?groupBy=shop_id&having=SUM(price)>40";
    public static final String LIMIT_3 = AUTHORITY + "firework?limit=3";
    public static final String UNIQUE_SEARCH = AUTHORITY + "firework?distinct=true";

}

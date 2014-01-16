package com.novoda.sqliteprovider.demo.simple.provider;

import android.net.Uri;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FireworkProvider extends SQLiteContentProviderImpl {

    private static final String AUTHORITY = "content://com.novoda.demo.simple/";
    private static final String TABLE_SHOP = "shop";
    public static final String COL_SHOP_NAME = "name";
    public static final String COL_SHOP_POSTCODE = "postcode";

    public static final Uri SHOPS = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_SHOP).build();
}

package com.novoda.sqliteprovider.demo.simple.provider;

import android.net.Uri;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FireworkProvider extends SQLiteContentProviderImpl {

    public static final String AUTHORITY = "content://com.novoda.demo.simple/";
    private static final String TABLE_SHOP = "shop";
    public static final Uri SELECT_SHOPS = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_SHOP).build();
}

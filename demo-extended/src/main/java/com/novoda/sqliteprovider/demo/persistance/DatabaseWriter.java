package com.novoda.sqliteprovider.demo.persistance;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.novoda.sqliteprovider.demo.provider.FireworkProvider;

public class DatabaseWriter {

    private static final String KEY_ALLOW_YIELD = "allowYield";
    private final ContentResolver contentResolver;

    public DatabaseWriter(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void saveDataToFireworksTable(ContentValues values) {
        saveDataToTable(DatabaseConstants.TBL_FIREWORKS, values);
    }

    public void bulkSaveDataToFireworksTable(ContentValues[] values) {
        bulkSaveDataToTable(DatabaseConstants.TBL_FIREWORKS, values, "true");
    }

    public void bulkSaveDataToFireworksTableWithoutYield(ContentValues[] values) {
        bulkSaveDataToTable(DatabaseConstants.TBL_FIREWORKS, values, "false");
    }

    private void bulkSaveDataToTable(String table, ContentValues[] values, String allowYield) {
        Uri uri = createUri(table).buildUpon().appendQueryParameter(KEY_ALLOW_YIELD, allowYield).build();
        contentResolver.bulkInsert(uri, values);
    }

    private void saveDataToTable(String table, ContentValues values) {
        Uri uri = createUri(table);
        contentResolver.insert(uri, values);
    }

    private Uri createUri(String tableName) {
        return Uri.parse(FireworkProvider.AUTHORITY + tableName);
    }
}

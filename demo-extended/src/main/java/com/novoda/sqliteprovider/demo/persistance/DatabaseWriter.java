package com.novoda.sqliteprovider.demo.persistance;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.novoda.sqliteprovider.demo.provider.FireworkProvider;

public class DatabaseWriter {

    private final ContentResolver contentResolver;

    public DatabaseWriter(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void saveDataToFireworksTable(ContentValues values) {
        saveDataToTable(DatabaseConstants.TBL_FIREWORKS, values);
    }

    public void bulkSaveDataToFireworksTable(ContentValues[] values) {
        bulkSaveDataToTable(DatabaseConstants.TBL_FIREWORKS, values);
    }

    private void bulkSaveDataToTable(String table, ContentValues[] values) {
        Uri uri = createUri(table);
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

package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;
import static com.novoda.sqliteprovider.demo.provider.FireworkProvider.AUTHORITY;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;


public class DatabaseWriter {

    private final ContentResolver contentResolver;

    public DatabaseWriter(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void saveDataToFireworksTable(ContentValues values) {
        saveDataToTable(TBL_FIREWORKS, values);
    }

    private void saveDataToTable(String table, ContentValues values) {
        Uri uri = createUri(table);
        contentResolver.insert(uri, values);
    }

    private Uri createUri(String tableName) {
        return Uri.parse(AUTHORITY + tableName);
    }
}

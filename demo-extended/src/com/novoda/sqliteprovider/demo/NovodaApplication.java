package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;
import com.novoda.sqliteprovider.demo.persistance.DatabaseWriter;
import com.novoda.sqliteprovider.demo.persistance.FireworkReader;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;

public class NovodaApplication extends Application {

    private DatabaseReader databaseReader;
    private DatabaseWriter databaseWriter;
    private FireworkReader fireworkReader;
    private FireworkWriter fireworkWriter;

    private DatabaseReader getDatabaseReader() {
        if (databaseReader == null) {
            databaseReader = new DatabaseReader(getContentResolver());
        }
        return databaseReader;
    }

    private DatabaseWriter getDatabaseWriter() {
        if (databaseWriter == null) {
            databaseWriter = new DatabaseWriter(getContentResolver());
        }
        return databaseWriter;
    }

    public final FireworkReader getFireworkReader() {
        if (fireworkReader == null) {
            fireworkReader = new FireworkReader(getDatabaseReader());
        }
        return fireworkReader;
    }

    public final FireworkWriter getFireworkWriter() {
        if (fireworkWriter == null) {
            fireworkWriter = new FireworkWriter(getDatabaseWriter());
        }
        return fireworkWriter;
    }
}

package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.Fireworks.*;

import android.content.ContentValues;

import com.novoda.sqliteprovider.demo.domain.Firework;

public class FireworkWriter {

    private final DatabaseWriter databaseWriter;

    public FireworkWriter(DatabaseWriter databaseWriter) {
        this.databaseWriter = databaseWriter;
    }

    public void saveFirework(Firework firework) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, firework.getName());
        values.put(COL_COLOR, firework.getColor());
        values.put(COL_NOISE, firework.getNoise());
        values.put(COL_TYPE, firework.getType());
        values.put(COL_PRICE, firework.getPrice());
        values.put(COL_SHOP, 1);

        databaseWriter.saveDataToFireworksTable(values);
    }

}

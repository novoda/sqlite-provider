package com.novoda.sqliteprovider.demo.persistance;

import android.database.Cursor;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Groups;
import com.novoda.sqliteprovider.demo.domain.Groups.Group;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.Fireworks;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;
import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_SHOPS;

public class FireworkReader {

    private final DatabaseReader databaseReader;

    public FireworkReader(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public Firework getFirework(int primaryKey) {
        Cursor cursor = databaseReader.getFrom(TBL_FIREWORKS, primaryKey);

        if (cursor.moveToFirst()) {
            Firework firework = getFirework(cursor);

            cursor.close();

            return firework;
        }
        Log.e("No data in the cursor. Returning null safe firework.");
        return Firework.getNullSafeFirework();
    }

    public List<Firework> getFireworksForShop(int primaryKey) {
        Cursor cursor = databaseReader.getChildren(TBL_SHOPS, TBL_FIREWORKS, primaryKey);

        List<Firework> fireworks = populateListWith(cursor);

        cursor.close();

        return fireworks;
    }

    public List<Firework> getLimitedNumberOfFireworks(int limit) {
        Cursor cursor = databaseReader.getLimited(TBL_FIREWORKS, limit);

        List<Firework> fireworks = populateListWith(cursor);

        cursor.close();

        return fireworks;
    }

    public List<Firework> getUniqueFireworks() {
        String[] selection = {Fireworks.COL_NAME, Fireworks.COL_TYPE, Fireworks.COL_COLOR, Fireworks.COL_NOISE, Fireworks.COL_PRICE};

        Cursor cursor = databaseReader.getDistinct(TBL_FIREWORKS, selection);

        List<Firework> fireworks = populateListWith(cursor);

        cursor.close();

        return fireworks;
    }

    public List<Firework> getAll() {
        Cursor cursor = databaseReader.getAllFrom(TBL_FIREWORKS);

        List<Firework> fireworks = populateListWith(cursor);

        cursor.close();

        return fireworks;
    }

    private List<Firework> populateListWith(Cursor cursor) {
        List<Firework> data = new ArrayList<Firework>();
        if (cursor.moveToFirst()) {
            do {
                Firework firework = getFirework(cursor);
                data.add(firework);
            } while (cursor.moveToNext());
        } else {
            Log.e("No data in the cursor.");
        }
        return data;
    }

    private Firework getFirework(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Fireworks.COL_NAME));
        String color = cursor.getString(cursor.getColumnIndexOrThrow(Fireworks.COL_COLOR));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(Fireworks.COL_NOISE));
        String noise = cursor.getString(cursor.getColumnIndexOrThrow(Fireworks.COL_TYPE));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(Fireworks.COL_PRICE));

        return new Firework(name, color, type, noise, price);
    }

    public Groups getShopsWithFireworkPricesAddingUpToOverForty() {
        String group = Fireworks.COL_SHOP;
        String having = "SUM (" + Fireworks.COL_PRICE + ") > 40";
        String[] selection = {Fireworks.COL_SHOP, "SUM (" + Fireworks.COL_PRICE + ") as total"};
        Cursor cursor = databaseReader.getGroupedByAndHaving(TBL_FIREWORKS, group, having, selection);

        List<Group> data = populateGroupListWith(cursor);

        cursor.close();

        return new Groups(data);
    }

    private List<Group> populateGroupListWith(Cursor cursor) {
        List<Group> data = new ArrayList<Group>();
        if (cursor.moveToFirst()) {
            do {
                double count = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                int shopId = cursor.getInt(cursor.getColumnIndexOrThrow(Fireworks.COL_SHOP));
                data.add(new Group(count, shopId));
            } while (cursor.moveToNext());
        } else {
            Log.e("No data in the cursor.");
        }
        return data;
    }
}

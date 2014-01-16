package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.FireworkReader;

import java.util.List;

public class FireworkLoader extends AsyncTaskLoader<List<Firework>> {

    public static final int LOADER_ID = 123;

    private final FireworkReader fireworkReader;

    public FireworkLoader(Context context, FireworkReader fireworkReader) {
        super(context);
        this.fireworkReader = fireworkReader;
        forceLoad();
    }

    @Override
    public List<Firework> loadInBackground() {
        return fireworkReader.getAll();
    }
}

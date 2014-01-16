package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;

public class FireworkSaver extends AsyncTaskLoader<Firework> {

    private final FireworkWriter writer;
    private final Firework firework;
    public static final int LOADER_ID = 123;

    public FireworkSaver(Context context, FireworkWriter writer, Firework firework) {
        super(context);
        this.writer = writer;
        this.firework = firework;
        forceLoad();
    }

    @Override
    public Firework loadInBackground() {
        writer.saveFirework(firework);
        return firework;
    }


}

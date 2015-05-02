package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;

import java.util.List;

public class FireworksBulkSaver extends AsyncTaskLoader<List<Firework>> {

    private final FireworkWriter writer;
    private final List<Firework> fireworks;
    public static final int LOADER_ID = 123;

    public FireworksBulkSaver(Context context, FireworkWriter writer, List<Firework> fireworks) {
        super(context);
        this.writer = writer;
        this.fireworks = fireworks;
        forceLoad();
    }

    @Override
    public List<Firework> loadInBackground() {
        writer.saveFireworks(fireworks);
        return fireworks;
    }

}

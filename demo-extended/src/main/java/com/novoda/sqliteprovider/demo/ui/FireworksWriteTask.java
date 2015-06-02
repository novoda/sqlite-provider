package com.novoda.sqliteprovider.demo.ui;

import android.os.AsyncTask;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;

import java.util.Arrays;
import java.util.List;

class FireworksWriteTask extends AsyncTask<Firework, Void, Void> {

    private final String name;
    private final FireworkWriter writer;
    private final boolean allowYield;
    private final Listener listener;

    private long startTime;

    FireworksWriteTask(String name, FireworkWriter writer, boolean allowYield, Listener listener) {
        this.name = name;
        this.writer = writer;
        this.allowYield = allowYield;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Firework... params) {
        List<Firework> fireworks = Arrays.asList(params);
        if (allowYield) {
            writer.saveFireworks(fireworks);
        } else {
            writer.saveFireworksWithoutYield(fireworks);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void fireworks) {
        listener.onFinish(name, System.currentTimeMillis() - startTime);
    }

    interface Listener {
        void onFinish(String name, long duration);
    }
}

package com.novoda.sqliteprovider.demo.simple.ui;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class SimpleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

package com.novoda.sqliteprovider.demo;

import android.app.Application;

import com.novoda.sqliteprovider.demo.persistance.*;

public class NovodaApplication extends Application {

	private DatabaseReader databaseReader;
	private DatabaseWriter databaseWriter;
	private FireworkReader fireworkReader;
	private FireworkWriter fireworkWriter;
	private CacheUriListener cacheUriListener;
	
	private DatabaseReader getDatabaseReader() {
		if(databaseReader == null){
			databaseReader = new DatabaseReader(getContentResolver(), getCachedUriListener());
		}
		return databaseReader;
	}
	
	private DatabaseWriter getDatabaseWriter() {
		if(databaseWriter == null){
			databaseWriter = new DatabaseWriter(getContentResolver(), getCachedUriListener());
		}
		return databaseWriter;
	}
	
	public FireworkReader getFireworkReader() {
		if(fireworkReader == null){
			fireworkReader = new FireworkReader(getDatabaseReader());
		}
		return fireworkReader;
	}
	
	public FireworkWriter getFireworkWriter() {
		if(fireworkWriter == null){
			fireworkWriter = new FireworkWriter(getDatabaseWriter());
		}
		return fireworkWriter;
	}
	
	public CacheUriListener getCachedUriListener(){
		if(cacheUriListener == null){
			cacheUriListener = new CacheUriListener();
		}
		return cacheUriListener;
	}
}

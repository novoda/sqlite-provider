package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.ArrayList;
import java.util.List;

public class FireworkLoader extends AsyncTaskLoader<List<Firework>> {

	private final SQLiteOpenHelper sqliteHelper;

	public FireworkLoader(Context context, SQLiteOpenHelper sqliteHelper) {
		super(context);
		this.sqliteHelper = sqliteHelper;
		forceLoad();
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
	}

	@Override
	public List<Firework> loadInBackground() {
		ArrayList<Firework> data = new ArrayList<Firework>();
		data.add(new Firework("name", "color", "type", "noise"));
		return data;
	}
	
	

}

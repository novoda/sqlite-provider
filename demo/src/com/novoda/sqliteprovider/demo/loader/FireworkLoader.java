package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.List;

public class FireworkLoader extends AsyncTaskLoader<List<Firework>> {

	public FireworkLoader(Context context) {
		super(context);
		forceLoad();
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
	}

	@Override
	public List<Firework> loadInBackground() {
		return null;
	}
	
	

}

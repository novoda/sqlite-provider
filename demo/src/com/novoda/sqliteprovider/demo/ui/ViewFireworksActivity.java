package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.loader.FireworkLoader;
import com.novoda.sqliteprovider.demo.ui.adapter.FireworkAdapter;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.List;

public class ViewFireworksActivity extends NovodaActivity implements LoaderCallbacks<List<Firework>>{

	private ListView listview;
	private final Handler uiHook = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_fireworks);
		
		listview = (ListView) findViewById(android.R.id.list);
		
		getSupportLoaderManager().initLoader(123, null, this);
	}

	public Loader<List<Firework>> onCreateLoader(int id, Bundle args) {
		Log.i("Loading fireworks into activity");
		return new FireworkLoader(this, getApp().getDatabaseReader());
	}

	public void onLoadFinished(Loader<List<Firework>> loader, List<Firework> data) {
		Log.i("Finished loading fireworks");
		updateList(new FireworkAdapter(this, data));
	}

	private void updateList(final ArrayAdapter<Firework> adapter) {
		Log.i("updating activity list");
		uiHook.post(new Runnable() {
			public void run() {
				listview.setAdapter(adapter);
			}
		});
	}

	public void onLoaderReset(Loader<List<Firework>> loader) {
		
	}
	
}

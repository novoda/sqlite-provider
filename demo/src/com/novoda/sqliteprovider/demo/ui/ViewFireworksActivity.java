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
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

import java.util.ArrayList;
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
		return new FireworkLoader(this);
	}

	public void onLoadFinished(Loader<List<Firework>> loader, List<Firework> data) {
		data = new ArrayList<Firework>();
		data.add(new Firework("name", "color", "type", "noise"));
		
		final ArrayAdapter<Firework> adapter = new ArrayAdapter<Firework>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
	
		uiHook.post(new Runnable() {
			public void run() {
				listview.setAdapter(adapter);
			}
		});
	}

	public void onLoaderReset(Loader<List<Firework>> loader) {
		
	}
	
}

package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.loader.FireworkLoader;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class ViewFireworksActivity extends NovodaActivity implements LoaderCallbacks<Object>{

	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_fireworks);
		
		listview = (ListView) findViewById(android.R.id.list);
		
		getSupportLoaderManager().initLoader(123, null, this);
	}

	public Loader<Object> onCreateLoader(int id, Bundle args) {
		return new FireworkLoader(this);
	}

	public void onLoadFinished(Loader<Object> loader, Object data) {
		
	}

	public void onLoaderReset(Loader<Object> loader) {
		
	}
	
}

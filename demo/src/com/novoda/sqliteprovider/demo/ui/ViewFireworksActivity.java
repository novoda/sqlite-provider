package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class ViewFireworksActivity extends NovodaActivity {

	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_fireworks);
		
		listview = (ListView) findViewById(android.R.id.list);
	}
	
}

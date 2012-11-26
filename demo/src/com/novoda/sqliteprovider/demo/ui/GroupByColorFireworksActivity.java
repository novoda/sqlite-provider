package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;

import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class GroupByColorFireworksActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getApp().getFireworkReader().getFireworksByColor();
	}
	
}
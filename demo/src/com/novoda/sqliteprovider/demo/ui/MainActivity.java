package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class MainActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onViewAllFireworksClick(View button){
		Intent intent = new Intent(this, ViewFireworksActivity.class);
		startActivity(intent);
	}
}

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

	public void onViewAllFireworksClick(View button) {
		Intent intent = new Intent(this, ViewFireworksActivity.class);
		startActivity(intent);
	}

	public void onAddFireworkClick(View button) {
		Intent intent = new Intent(this, AddFireworkActivity.class);
		startActivity(intent);
	}

	public void onFindFireworkWithPrimaryKeyClick(View button) {

	}

	public void onFindAllFireworksFromOneShopClick(View button) {

	}

	public void onFindAllRedFireworksClick(View button) {

	}

	public void onFindAllWizzFireworksClick(View button) {

	}

	public void onFindThreeFireworkShopsClick(View button) {

	}

	public void onFindUniquelyNoisyFireworksClick(View button) {

	}
}

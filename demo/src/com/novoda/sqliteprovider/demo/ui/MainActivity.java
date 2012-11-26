package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.util.FromXML;

public class MainActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@FromXML
	public void onViewAllFireworksClick(View button) {
		Intent intent = new Intent(this, ViewFireworksActivity.class);
		startActivity(intent);
	}

	@FromXML
	public void onAddFireworkClick(View button) {
		Intent intent = new Intent(this, AddFireworkActivity.class);
		startActivity(intent);
	}

	@FromXML
	public void onFindFireworkWithPrimaryKeyClick(View button) {

	}

	@FromXML
	public void onFindAllFireworksFromOneShopClick(View button) {

	}

	@FromXML
	public void onFindAllRedFireworksClick(View button) {

	}

	@FromXML
	public void onFindAllWizzFireworksClick(View button) {

	}

	@FromXML
	public void onFindThreeFireworkShopsClick(View button) {

	}

	@FromXML
	public void onFindUniquelyNoisyFireworksClick(View button) {

	}
}

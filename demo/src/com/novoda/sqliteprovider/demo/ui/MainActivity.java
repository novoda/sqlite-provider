package com.novoda.sqliteprovider.demo.ui;

import android.app.Activity;
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
		startActivity(ViewAllFireworksActivity.class);
	}

	@FromXML
	public void onAddFireworkClick(View button) {
		startActivity(AddFireworkActivity.class);
	}

	@FromXML
	public void onFindFireworkWithPrimaryKeyClick(View button) {
		startActivity(FindFireworkWithPkActivity.class);
	}

	@FromXML
	public void onFindAllFireworksFromOneShopClick(View button) {
		startActivity(FindFireworksFromOneShopActivity.class);
	}

	@FromXML
	public void onGroupFireworksByTypeAndHavingColorRedClick(View button) {
		startActivity(FindRedFireworksActivity.class);
	}

	@FromXML
	public void onFindThreeFireworkShopsClick(View button) {
		startActivity(FindThreeFireworksActivity.class);
	}

	@FromXML
	public void onFindUniquelyNoisyFireworksClick(View button) {

	}
	
	private void startActivity(Class<? extends Activity> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}
}

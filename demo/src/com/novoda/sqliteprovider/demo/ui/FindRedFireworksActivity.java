package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

import java.util.List;

public class FindRedFireworksActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<Firework> fireworks = getApp().getFireworkReader().getRedFireworksGroupedByType();
		
		Shop shop = new Shop("Red Fireworks", "List below is Red fireworks grouped by Type", fireworks);
		
		view(shop);
		finish();
	}
	
	private void view(Shop shop) {
		Intent intent = new Intent(this, ViewShopActivity.class);
		intent.putExtra(ViewShopActivity.EXTRA_SHOP, shop);
		startActivity(intent);
	}
	
}
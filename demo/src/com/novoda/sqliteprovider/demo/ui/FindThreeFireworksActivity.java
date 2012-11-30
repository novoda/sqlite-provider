package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

import java.util.List;

public class FindThreeFireworksActivity extends NovodaActivity {

	private static final int LIMIT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<Firework> fireworks = getApp().getFireworkReader().getLimitedNumberOfFireworks(LIMIT);
		
		Shop shop = new Shop("3 Fireworks", "List below is limited to first 3 results", fireworks);
		
		view(shop);
		finish();
	}
	
	private void view(Shop shop) {
		Intent intent = new Intent(this, FindFireworkUsingShopActivity.class);
		intent.putExtra(FindFireworkUsingShopActivity.EXTRA_SHOP, shop);
		startActivity(intent);
	}
	
}

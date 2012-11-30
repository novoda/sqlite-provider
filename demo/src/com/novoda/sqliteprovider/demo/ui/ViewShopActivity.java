package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.*;
import com.novoda.sqliteprovider.demo.ui.adapter.FireworkAdapter;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;
import com.novoda.sqliteprovider.demo.util.Log;

public class ViewShopActivity extends NovodaActivity {

	protected static final String EXTRA_SHOP = "com.novoda.sqliteprovider.demo.ui.EXTRA_SHOP";
	protected static final String EXTRA_INFO = "com.novoda.sqliteprovider.demo.ui.EXTRA_INFO";
	
	private ListView listview;
	private UseCaseInfo info;
	private UriSqlFragment uriSqlFragment;
	private Shop shop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_shop);
		
		info = (UseCaseInfo) getIntent().getSerializableExtra(EXTRA_INFO);
		uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
		setExampleInfo();
		
		shop = (Shop) getIntent().getSerializableExtra(EXTRA_SHOP);
		setShopData();
	}

	private void setExampleInfo() {
		if(info != null){
			uriSqlFragment.setUri(info.getUri());
			uriSqlFragment.setSql(info.getSql());
		} else {
			uriSqlFragment.hide();
		}
	}
	
	private void setShopData() {
		if(shop != null){
			TextView shopNameTextView = (TextView) findViewById(R.id.activity_view_shop_name);
			shopNameTextView.setText(shop.getName());
			
			TextView shopPostcodeTextView = (TextView) findViewById(R.id.activity_view_shop_postcode);
			shopPostcodeTextView.setText(shop.getPostcode());
			
			listview = (ListView) findViewById(android.R.id.list);
			listview.setOnItemClickListener(onFireworkListItemClick);
			listview.setAdapter(new FireworkAdapter(this, shop.getFireworks()));
		} else {
			Log.e("No firework found in the intent");
		}
	}

	private final OnItemClickListener onFireworkListItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			viewFirework((Firework)listview.getItemAtPosition(position));
		}
	};
	
	private void viewFirework(Firework firework) {
		Intent intent = new Intent(this, FireworkActivity.class);
		intent.putExtra(FireworkActivity.EXTRA_FIREWORK, firework);
		startActivity(intent);
	}
}
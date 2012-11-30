package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.RawSql;
import com.novoda.sqliteprovider.demo.provider.FireworkUriConstants;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.util.FromXML;
import com.novoda.sqliteprovider.demo.ui.widget.UriSqlView;

import java.util.List;

public class FindFireworksFromOneShopActivity extends NovodaActivity {

	private EditText primaryKeyEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_firework_from_one_shop);
		
		primaryKeyEditText = (EditText) findViewById(R.id.find_fireworks_from_one_shop_input_shop_primary_key);
		
		UriSqlView uriSqlView = (UriSqlView) findViewById(R.id.view_uri_sql);
		uriSqlView.setUri(FireworkUriConstants.ONE_TO_MANY_SEARCH);
		uriSqlView.setSql(RawSql.SELECT_USING_SHOP_FOREIGN_KEY);
	}
	
	@FromXML
	public void onFindShopFireworks(View button){
		if(userHasEnteredSomething()){
			try {
				int primaryKey = getPrimaryKey();

				List<Firework> fireworks = getApp().getFireworkReader().getFireworksForShop(primaryKey);
				
				Shop shop = new Shop("", "Below are the Fireworks with shop primary key: "+ primaryKey, fireworks);
				
				view(shop);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "Primary Key should be an int", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean userHasEnteredSomething() {
		return !TextUtils.isEmpty(primaryKeyEditText.getText());
	}
	
	private int getPrimaryKey() {
		return Integer.parseInt(primaryKeyEditText.getText().toString());
	}
	
	private void view(Shop shop) {
		Intent intent = new Intent(this, ViewShopActivity.class);
		intent.putExtra(ViewShopActivity.EXTRA_SHOP, shop);
		startActivity(intent);
	}
}

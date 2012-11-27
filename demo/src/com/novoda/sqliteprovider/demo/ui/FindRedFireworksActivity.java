package com.novoda.sqliteprovider.demo.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.domain.Groups;
import com.novoda.sqliteprovider.demo.domain.Groups.Group;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class FindRedFireworksActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Groups groups = getApp().getFireworkReader().getCountOfRedFireworksGroupedByShop();
		
		view(groups);
	}
	
	private void view(Groups groups) {
		LinearLayout view = new LinearLayout(this);
		view.setOrientation(LinearLayout.VERTICAL);
		for(Group group : groups){
			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView shopTextView = new TextView(this);
			shopTextView.setText("Shop ID: "+group.getShopId()+"  ");
			row.addView(shopTextView, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

			TextView countTextView = new TextView(this);
			countTextView.setText("Red Firework count: "+group.getCount());
			row.addView(countTextView, new LayoutParams(WRAP_CONTENT, MATCH_PARENT));
			
			view.addView(row, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		}
		addContentView(view, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
	}
	
}
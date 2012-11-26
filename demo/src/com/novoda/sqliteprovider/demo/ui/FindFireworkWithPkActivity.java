package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.view.View;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.util.FromXML;

public class FindFireworkWithPkActivity extends NovodaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_firework_with_pk);
	}
	
	@FromXML
	public void onFindFireworkWithPkClick(View button){
		
	}
}

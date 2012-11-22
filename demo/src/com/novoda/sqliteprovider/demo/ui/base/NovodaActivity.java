package com.novoda.sqliteprovider.demo.ui.base;

import android.support.v4.app.FragmentActivity;

import com.novoda.sqliteprovider.demo.NovodaApplication;

public class NovodaActivity extends FragmentActivity {

	protected NovodaApplication getApp(){
		return (NovodaApplication) getApplication();
	}
	
}

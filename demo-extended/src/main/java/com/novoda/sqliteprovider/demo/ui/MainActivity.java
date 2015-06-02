package com.novoda.sqliteprovider.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.input.DemoMenu;
import com.novoda.sqliteprovider.demo.ui.util.FromXML;

public class MainActivity extends NovodaActivity implements DemoMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    @FromXML
    public void onViewAllFireworksClick(View button) {
        startActivity(ViewAllFireworksActivity.class);
    }

    @Override
    @FromXML
    public void onAddFireworkClick(View button) {
        startActivity(AddFireworkActivity.class);
    }

    @Override
    @FromXML
    public void onBulkAddFireworksClick(View view) {
        startActivity(AddBulkFireworksActivity.class);
    }

    @Override
    @FromXML
    public void onBulkAddYieldFireworksClick(View view) {
        startActivity(AddBulkYieldFireworksActivity.class);
    }

    @Override
    @FromXML
    public void onFindFireworkWithPrimaryKeyClick(View button) {
        startActivity(FindFireworkWithPkActivity.class);
    }

    @Override
    @FromXML
    public void onFindAllFireworksFromOneShopClick(View button) {
        startActivity(FindFireworksFromOneShopActivity.class);
    }

    @Override
    @FromXML
    public void onGroupFireworksByTypeAndHavingColorRedClick(View button) {
        startActivity(WellStockedShopActivity.class);
    }

    @Override
    @FromXML
    public void onFindThreeFireworkShopsClick(View button) {
        startActivity(FindThreeFireworksActivity.class);
    }

    @Override
    @FromXML
    public void onFindUniqueFireworksClick(View button) {
        startActivity(FindDistinctFireworksActivity.class);
    }

    private void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}

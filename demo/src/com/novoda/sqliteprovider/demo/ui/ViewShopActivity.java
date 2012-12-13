package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.*;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory.UseCase;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.*;
import com.novoda.sqliteprovider.demo.ui.fragment.ShopFragment.OnFireworkClickListener;

public class ViewShopActivity extends NovodaActivity implements OnFireworkClickListener {

    protected static final String EXTRA_SHOP = "com.novoda.sqliteprovider.demo.ui.EXTRA_SHOP";
    protected static final String EXTRA_INFO = "com.novoda.sqliteprovider.demo.ui.EXTRA_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop);

        UseCase useCase = (UseCase) getIntent().getSerializableExtra(EXTRA_INFO);
        Shop shop = (Shop) getIntent().getSerializableExtra(EXTRA_SHOP);

        FragmentManager fragmentManager = getSupportFragmentManager();

        UriSqlFragment uriSqlFragment = (UriSqlFragment) fragmentManager.findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(useCase);

        ShopFragment shopFragment = (ShopFragment) fragmentManager.findFragmentById(R.id.fragment_shop);
        shopFragment.setShop(shop);
    }

    @Override
    public void onFireworkClick(Firework firework) {
        viewFirework(firework);
    }

    private void viewFirework(Firework firework) {
        Intent intent = new Intent(this, FireworkActivity.class);
        intent.putExtra(FireworkActivity.EXTRA_FIREWORK, firework);
        startActivity(intent);
    }
}

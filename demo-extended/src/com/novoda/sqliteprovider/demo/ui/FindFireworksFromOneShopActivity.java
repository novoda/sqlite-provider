package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.FindFireworksFromOneShopFragment.OnShopFound;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;
import com.novoda.sqliteprovider.demo.ui.input.OnPrimaryKeyInputError;

public class FindFireworksFromOneShopActivity extends NovodaActivity implements OnPrimaryKeyInputError, OnShopFound {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_firework_from_one_shop);

        UriSqlFragment uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(UseCaseFactory.UseCase.ONE_TO_MANY);
    }

    @Override
    public void onPrimaryKeyInvalid() {
        Toast.makeText(this, "Primary Key should be an int", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShopFound(Shop shop) {
        view(shop);
    }

    private void view(Shop shop) {
        Intent intent = new Intent(this, ViewShopActivity.class);
        intent.putExtra(ViewShopActivity.EXTRA_SHOP, shop);
        intent.putExtra(ViewShopActivity.EXTRA_INFO, UseCaseFactory.UseCase.ONE_TO_MANY);
        startActivity(intent);
    }
}

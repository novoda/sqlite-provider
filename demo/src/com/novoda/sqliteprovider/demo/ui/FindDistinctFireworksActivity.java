package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;

import com.novoda.sqliteprovider.demo.domain.*;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory.UseCase;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

import java.util.List;

public class FindDistinctFireworksActivity extends NovodaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Firework> fireworks = getApp().getFireworkReader().getUniqueFireworks();

        Shop shop = new Shop("Unique Fireworks", "List below is any distinct firework", fireworks);

        view(shop);
        finish();
    }

    private void view(Shop shop) {
        Intent intent = new Intent(this, ViewShopActivity.class);
        intent.putExtra(ViewShopActivity.EXTRA_SHOP, shop);
        intent.putExtra(ViewShopActivity.EXTRA_INFO, UseCase.DISTINCT);
        startActivity(intent);
    }
}

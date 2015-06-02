package com.novoda.sqliteprovider.demo.ui.input;

import android.view.View;

public interface DemoMenu {

    void onViewAllFireworksClick(View button);

    void onAddFireworkClick(View button);

    void onBulkAddFireworksClick(View button);

    void onFindFireworkWithPrimaryKeyClick(View button);

    void onFindAllFireworksFromOneShopClick(View button);

    void onGroupFireworksByTypeAndHavingColorRedClick(View button);

    void onFindThreeFireworkShopsClick(View button);

    void onFindUniqueFireworksClick(View button);

    void onBulkAddYieldFireworksClick(View button);

}

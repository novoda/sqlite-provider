package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Groups;
import com.novoda.sqliteprovider.demo.domain.Groups.Group;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants;
import com.novoda.sqliteprovider.demo.provider.FireworkUriConstants;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.widget.UriSqlView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class WellStockedShopActivity extends NovodaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Groups groups = getApp().getFireworkReader().getShopsWithFireworkPricesAddingUpToOverForty();

        view(groups);
    }

    private void view(Groups groups) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        addUriSqlView(root);

        for (Group group : groups) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView shopTextView = new TextView(this);
            shopTextView.setText("Shop ID: " + group.getShopId() + "  ");
            row.addView(shopTextView, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            TextView countTextView = new TextView(this);
            countTextView.setText("Total: " + group.getFormattedTotal());
            row.addView(countTextView, new LayoutParams(WRAP_CONTENT, MATCH_PARENT));

            root.addView(row, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        addContentView(root, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    private void addUriSqlView(LinearLayout root) { // TODO change to Fragment
        View.inflate(this, R.layout.view_uri_sql, root);

        UriSqlView uriSqlView = (UriSqlView) root.findViewById(R.id.view_uri_sql);
        uriSqlView.setUri(FireworkUriConstants.GROUP_BY_SEARCH);
        uriSqlView.setSql(DatabaseConstants.RawSql.GROUP_BY);
    }

}

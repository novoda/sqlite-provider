package com.novoda.sqliteprovider.demo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.R;

public class UriSqlView extends LinearLayout {

    private TextView uriTextView;
    private TextView sqlTextView;

    public UriSqlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UriSqlView(Context context) {
        super(context);
        init();
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        uriTextView = (TextView) findViewById(R.id.view_uri_sql_uri);
        sqlTextView = (TextView) findViewById(R.id.view_uri_sql_sql);
    }

    public void setUri(String uri) {
        if (uriTextView != null) {
            uriTextView.setText(uri);
        }
    }

    public void setSql(String sql) {
        if (sqlTextView != null) {
            sqlTextView.setText(sql);
        }
    }
}

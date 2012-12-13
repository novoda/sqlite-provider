package com.novoda.sqliteprovider.demo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.*;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory.UseCase;
import com.novoda.sqliteprovider.demo.ui.widget.UriSqlView;

public class UriSqlFragment extends Fragment {

    private UriSqlView uriSqlView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_uri_sql, container, false);

        uriSqlView = (UriSqlView) root.findViewById(R.id.view_uri_sql);

        return root;
    }

    public void setInfo(UseCase useCase) {
        setInfo(UseCaseFactory.getInfo(useCase));
    }

    private void setInfo(UseCaseInfo info) {
        if (info.isValid()) {
            uriSqlView.setUri(info.getUri());
            uriSqlView.setSql(info.getSql());
        } else {
            hide();
        }
    }

    private void hide() {
        uriSqlView.setVisibility(View.GONE);
    }

    public void updateSql(String sql) {
        uriSqlView.setSql(sql);
    }
}

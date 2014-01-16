package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory.UseCase;
import com.novoda.sqliteprovider.demo.loader.FireworkLoader;
import com.novoda.sqliteprovider.demo.ui.adapter.FireworkAdapter;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.List;

public class ViewAllFireworksActivity extends NovodaActivity implements LoaderCallbacks<List<Firework>> {

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fireworks);

        listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(onFireworkListItemClick);

        UriSqlFragment uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(UseCase.SELECT_ALL);

        getSupportLoaderManager().initLoader(FireworkLoader.LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Firework>> onCreateLoader(int id, Bundle args) {
        Log.i("Loading fireworks into activity");
        return new FireworkLoader(this, getApp().getFireworkReader());
    }

    @Override
    public void onLoadFinished(Loader<List<Firework>> loader, List<Firework> data) {
        Log.i("Finished loading fireworks");
        updateList(new FireworkAdapter(this, data));
    }

    private void updateList(final ArrayAdapter<Firework> adapter) {
        Log.i("updating activity list");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listview.setAdapter(adapter);
            }
        });
    }

    private final OnItemClickListener onFireworkListItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            viewFirework((Firework) listview.getItemAtPosition(position));
        }
    };

    private void viewFirework(Firework firework) {
        Intent intent = new Intent(this, FireworkActivity.class);
        intent.putExtra(FireworkActivity.EXTRA_FIREWORK, firework);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<List<Firework>> loader) {
    }
}

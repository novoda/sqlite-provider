package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory;
import com.novoda.sqliteprovider.demo.loader.FireworksBulkSaver;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.AddBulkFireworksFragment.AddBulkFireworksListener;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;

import java.util.List;

public class AddBulkFireworksActivity extends NovodaActivity implements AddBulkFireworksListener, LoaderCallbacks<List<Firework>> {

    private UriSqlFragment uriSqlFragment;
    private List<Firework> fireworks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bulk_fireworks);

        uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(UseCaseFactory.UseCase.BULK_ADD);
    }

    @Override
    public void onEmptyInput() {
        Toast.makeText(this, "Fill in the number of fireworks to add", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBulkAddClick(List<Firework> fireworks) {
        this.fireworks = fireworks;
        getSupportLoaderManager().initLoader(FireworksBulkSaver.LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Firework>> onCreateLoader(int id, Bundle args) {
        return new FireworksBulkSaver(this, getApp().getFireworkWriter(), fireworks);
    }

    @Override
    public void onLoadFinished(Loader<List<Firework>> loader, List<Firework> data) {
        Toast.makeText(this, data.size() + " fireworks added!", Toast.LENGTH_LONG).show();
        uriSqlFragment.updateSql(createSQL(data));
    }

    private String createSQL(List<Firework> data) {
        Firework firework = data.get(0);
        return TextUtils.replace(
                DatabaseConstants.RawSql.BULK_INSERT_FIREWORKS,
                new String[]{"Times", "Na", "Co", "No", "Ft", "Pr", "Sh"},
                new CharSequence[]{
                        String.valueOf(data.size()),
                        firework.getName(),
                        firework.getColor(),
                        firework.getNoise(),
                        firework.getType(),
                        String.valueOf(firework.getPrice()), "1"
                }).toString();
    }

    @Override
    public void onLoaderReset(Loader<List<Firework>> loader) {
    }
}

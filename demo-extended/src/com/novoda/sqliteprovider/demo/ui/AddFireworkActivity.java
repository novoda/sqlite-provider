package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory;
import com.novoda.sqliteprovider.demo.loader.FireworkSaver;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.AddFireworkFragment.AddFireworkListener;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;

public class AddFireworkActivity extends NovodaActivity implements AddFireworkListener, LoaderCallbacks<Firework> {

    private UriSqlFragment uriSqlFragment;
    private Firework firework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_firework);

        uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(UseCaseFactory.UseCase.ADD);
    }

    @Override
    public void onEmptyInput() {
        Toast.makeText(this, "Fill in the firework", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick(Firework firework) {
        this.firework = firework;
        getSupportLoaderManager().initLoader(FireworkSaver.LOADER_ID, null, this);
    }

    @Override
    public Loader<Firework> onCreateLoader(int id, Bundle args) {
        return new FireworkSaver(this, getApp().getFireworkWriter(), firework);
    }

    @Override
    public void onLoadFinished(Loader<Firework> loader, Firework data) {
        Toast.makeText(this, "Firework that goes " + data.getNoise() + " added.", Toast.LENGTH_SHORT).show();

        uriSqlFragment.updateSql(createSQL(data));
    }

    private String createSQL(Firework data) {
        return TextUtils.replace(
                DatabaseConstants.RawSql.INSERT_FIREWORK,
                new String[]{"Na","Co","No", "Ft", "Pr","Sh"},
                new CharSequence[]{data.getName(), data.getColor(), data.getNoise(), data.getType(), String.valueOf(data.getPrice()), "1"})
                .toString();
    }

    @Override
    public void onLoaderReset(Loader<Firework> loader) {
    }
}

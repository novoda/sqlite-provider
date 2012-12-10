package com.novoda.sqliteprovider.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory.UseCase;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.FindFireworkWithPkFragment.OnFireworkFound;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;
import com.novoda.sqliteprovider.demo.ui.input.OnPrimaryKeyInputError;

public class FindFireworkWithPkActivity extends NovodaActivity implements OnFireworkFound, OnPrimaryKeyInputError {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_firework_with_pk);

        UriSqlFragment uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        uriSqlFragment.setInfo(UseCase.PRIMARY_KEY_LOOKUP);
    }

    @Override
    public void onFireworkFound(Firework firework) {
        view(firework);
    }

    private void view(Firework firework) {
        Intent intent = new Intent(this, FireworkActivity.class);
        intent.putExtra(FireworkActivity.EXTRA_FIREWORK, firework);
        startActivity(intent);
    }

    @Override
    public void onPrimaryKeyInvalid() {
        Toast.makeText(this, "Primary Key should be an Int", Toast.LENGTH_SHORT).show();
    }
}

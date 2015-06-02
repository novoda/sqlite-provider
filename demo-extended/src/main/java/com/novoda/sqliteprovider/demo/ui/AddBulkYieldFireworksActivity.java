package com.novoda.sqliteprovider.demo.ui;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.UseCaseFactory;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants;
import com.novoda.sqliteprovider.demo.persistance.DatabaseWriter;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.fragment.AddBulkYieldFireworksFragment;
import com.novoda.sqliteprovider.demo.ui.fragment.UriSqlFragment;

import java.util.List;

public class AddBulkYieldFireworksActivity extends NovodaActivity implements AddBulkYieldFireworksFragment.AddBulkYieldFireworksListener, FireworksWriteTask.Listener {

    private UriSqlFragment uriSqlFragment;
    private List<Firework> fireworks;
    private FireworkWriter writer;
    private AddBulkYieldFireworksFragment bulkInsertFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bulk_yield_fireworks);
        DatabaseWriter databaseWriter = new DatabaseWriter(getContentResolver());
        writer = new FireworkWriter(databaseWriter);
        uriSqlFragment = (UriSqlFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_uri_sql);
        bulkInsertFragment = (AddBulkYieldFireworksFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_add_bulk_yield_fireworks);
    }

    @Override
    public void onEmptyInput() {
        Toast.makeText(this, "Fill in the number of fireworks and threads!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBulkAddClick(List<Firework> fireworks, int numberOfThreads, boolean shouldYield) {
        this.fireworks = fireworks;
        Firework[] fireworksArray = fireworks.toArray(new Firework[fireworks.size()]);
        for (int i = 0; i < numberOfThreads; i++) {
            String name = "Thread - " + i + " items: " + fireworks.size() + " - allowYield: " + shouldYield;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new FireworksWriteTask(name, writer, shouldYield, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fireworksArray);
            } else {
                new FireworksWriteTask(name, writer, shouldYield, this).execute(fireworksArray);
            }
        }
        uriSqlFragment.setInfo(shouldYield ? UseCaseFactory.UseCase.BULK_YIELD_ADD : UseCaseFactory.UseCase.BULK_WIHOUT_YIELD);
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
    public void onFinish(String name, long duration) {
        uriSqlFragment.updateSql(createSQL(fireworks));
        String text = "Finished: " + name + " - took: " + duration + " millis";
        bulkInsertFragment.appendLog(text);
    }
}

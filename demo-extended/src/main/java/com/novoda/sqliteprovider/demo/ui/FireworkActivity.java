package com.novoda.sqliteprovider.demo.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.util.Log;

public class FireworkActivity extends NovodaActivity {

    protected static final String EXTRA_FIREWORK = "com.novoda.sqliteprovider.demo.EXTRA_FIREWORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firework);

        Firework firework = (Firework) getIntent().getSerializableExtra(EXTRA_FIREWORK);
        if (firework != null) {
            TextView fireworkNameTextView = (TextView) findViewById(R.id.firework_name);
            TextView fireworkColorTextView = (TextView) findViewById(R.id.firework_color);
            TextView fireworkTypeTextView = (TextView) findViewById(R.id.firework_type);
            TextView fireworkNoiseTextView = (TextView) findViewById(R.id.firework_noise);
            TextView fireworkPriceTextView = (TextView) findViewById(R.id.firework_price);
            TextView fireworkIdTextView = (TextView) findViewById(R.id.firework_id);
            TextView fireworkShopTextView = (TextView) findViewById(R.id.firework_shop);

            fireworkNameTextView.setText(firework.getName());
            fireworkColorTextView.setText(firework.getColor());
            fireworkTypeTextView.setText(firework.getType());
            fireworkNoiseTextView.setText(firework.getNoise());
            fireworkPriceTextView.setText(firework.getFormattedPrice());
            fireworkIdTextView.setText("Not implemented");
            fireworkShopTextView.setText("Not implemented");
        } else {
            Log.e("No firework found in the intent");
        }
    }

}

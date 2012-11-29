package com.novoda.sqliteprovider.demo.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.loader.FireworkSaver;
import com.novoda.sqliteprovider.demo.persistance.FireworkWriter;
import com.novoda.sqliteprovider.demo.persistance.UriListener;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.util.FromXML;
import com.novoda.sqliteprovider.demo.ui.widget.UriSqlView;

public class AddFireworkActivity extends NovodaActivity implements LoaderCallbacks<Firework> {

	private EditText nameEditText;
	private EditText colorEditText;
	private EditText noiseEditText;
	private EditText typeEditText;
	private EditText priceEditText;
	private UriSqlView uriSqlView;
	private Firework firework;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_firework);
		
		nameEditText = (EditText) findViewById(R.id.add_firework_input_name);
		colorEditText = (EditText) findViewById(R.id.add_firework_input_color);
		noiseEditText = (EditText) findViewById(R.id.add_firework_input_noise);
		typeEditText = (EditText) findViewById(R.id.add_firework_input_type);
		priceEditText = (EditText) findViewById(R.id.add_firework_input_price);
		
		uriSqlView = (UriSqlView) findViewById(R.id.view_uri_sql);
	}
	
	@FromXML
	public void onAddFireworkClick(View button){
		if(checkForInput(nameEditText) || checkForInput(colorEditText) ||
				checkForInput(noiseEditText) || checkForInput(typeEditText) || checkForInput(priceEditText)) {
			Toast.makeText(this, "Fill in the firework", Toast.LENGTH_SHORT).show();
			return;
		}

		String name = "";
		String color = "";
		String noise = "";
		String type = "";
		double price = 0;
		
		name = nameEditText.getText().toString();
		color = colorEditText.getText().toString();
		noise = noiseEditText.getText().toString();
		type = typeEditText.getText().toString();
		price = Double.parseDouble(priceEditText.getText().toString());
		
		firework = new Firework(name, color, type, noise, price);
		
		getSupportLoaderManager().initLoader(FireworkSaver.LOADER_ID, null, this);
	}

	private boolean checkForInput(EditText editText) {
		return TextUtils.isEmpty(editText.getText());
	}

	@Override
	public Loader<Firework> onCreateLoader(int id, Bundle args) {
		FireworkWriter fireworkWriter = getApp().getFireworkWriter();
		fireworkWriter.setUriListener(viewUriListener);
		return new FireworkSaver(this, fireworkWriter, firework);
	}

	private final UriListener viewUriListener = new UriListener() {
		@Override
		public void onUriSet(final Uri uri) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					uriSqlView.setUri(uri.toString());
				}
			});
		}
	};
	
	@Override
	public void onLoadFinished(Loader<Firework> loader, Firework data) {
		Toast.makeText(this, "Firework that goes "+ data.getNoise() +" added.", Toast.LENGTH_SHORT).show();
		
		nameEditText.setText("");
		colorEditText.setText("");
		noiseEditText.setText("");
		typeEditText.setText("");
		priceEditText.setText("");
	}

	@Override
	public void onLoaderReset(Loader<Firework> loader) {
	}
}
package com.novoda.sqliteprovider.demo.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.DatabaseWriter;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;

public class AddFireworkActivity extends NovodaActivity {

	private EditText nameEditText;
	private EditText colorEditText;
	private EditText noiseEditText;
	private EditText typeEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_firework);
		
		nameEditText = (EditText) findViewById(R.id.add_firework_input_name);
		colorEditText = (EditText) findViewById(R.id.add_firework_input_color);
		noiseEditText = (EditText) findViewById(R.id.add_firework_input_noise);
		typeEditText = (EditText) findViewById(R.id.add_firework_input_type);
	}
	
	public void onAddFireworkClick(View button){
		if(checkForInput(nameEditText) || checkForInput(colorEditText) ||
				checkForInput(noiseEditText) || checkForInput(typeEditText)) {
			Toast.makeText(this, "Fill in the firework", Toast.LENGTH_SHORT).show();
			return;
		}

		String name = "";
		String color = "";
		String noise = "";
		String type = "";
		
		name = nameEditText.getText().toString();
		color = colorEditText.getText().toString();
		noise = noiseEditText.getText().toString();
		type = typeEditText.getText().toString();
		
		Firework firework = new Firework(name, color, type, noise);
		
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("color", color);
		values.put("noise", noise);
		values.put("type", type);
		
		new DatabaseWriter(getContentResolver()).saveDataToFireworksTable(values);
		
		Toast.makeText(this, "Firework that goes "+ noise +" added.", Toast.LENGTH_SHORT).show();
		
		nameEditText.setText("");
		colorEditText.setText("");
		noiseEditText.setText("");
		typeEditText.setText("");
	}

	private boolean checkForInput(EditText editText) {
		return TextUtils.isEmpty(editText.getText());
	}
	
}

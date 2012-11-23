package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.DatabaseReader;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FireworkLoader extends AsyncTaskLoader<List<Firework>> {

	private final DatabaseReader databaseReader;

	public FireworkLoader(Context context, DatabaseReader databaseReader) {
		super(context);
		this.databaseReader = databaseReader;
		forceLoad();
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
	}

	@Override
	public List<Firework> loadInBackground() {
		ArrayList<Firework> data = new ArrayList<Firework>();
		
//		SQLiteContentProviderImpl impl = new SQLiteContentProviderImpl();
//		
//		Cursor cursor = impl.query(Uri.parse("content://com.novoda.demo/fireworks"), null, null, null, null);
//		// Cursor cursor = databaseReader.query("fireworks", new String[]{});
//
//		populateListWithCursor(data, cursor);
		
		data.add(new Firework("name", "color", "type", "noise"));
		
		return data;
	}

	private void populateListWithCursor(ArrayList<Firework> data, Cursor cursor) {
		if(cursor.moveToFirst()){
			do {
				String name = cursor.getString(1);
				String color = cursor.getString(2);
				String type = cursor.getString(3);
				String noise = cursor.getString(4);
				Firework firework = new Firework(name, color, type, noise);
				data.add(firework);
			} while(cursor.moveToNext());
		} else {
			Log.e("Database fail");
		}
	}
}

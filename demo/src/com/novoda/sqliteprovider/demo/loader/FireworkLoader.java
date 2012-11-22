package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FireworkLoader extends AsyncTaskLoader<List<Firework>> {

	private final SQLiteOpenHelper sqliteHelper;

	public FireworkLoader(Context context, SQLiteOpenHelper sqliteHelper) {
		super(context);
		this.sqliteHelper = sqliteHelper;
		forceLoad();
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
	}

	@Override
	public List<Firework> loadInBackground() {
		Cursor cursor = sqliteHelper.getWritableDatabase().query("fireworks", null, null, new String[]{}, null, null, null);
//		Cursor cursor = sqliteHelper.getWritableDatabase().rawQuery("SELECT * FROM fireworks", null);
		ArrayList<Firework> data = new ArrayList<Firework>();
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
		data.add(new Firework("name", "color", "type", "noise"));
		return data;
	}
	
	

}

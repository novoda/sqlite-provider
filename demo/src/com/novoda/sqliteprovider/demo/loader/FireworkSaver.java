package com.novoda.sqliteprovider.demo.loader;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.persistance.DatabaseWriter;

public class FireworkSaver extends AsyncTaskLoader<Firework> {

	private final DatabaseWriter writer;
	private final Firework firework;

	public FireworkSaver(Context context, DatabaseWriter writer, Firework firework) {
		super(context);
		this.writer = writer;
		this.firework = firework;
		forceLoad();
	}

	@Override
	public Firework loadInBackground() {
		
		ContentValues values = new ContentValues();
		values.put("name", firework.getName());
		values.put("color", firework.getColor());
		values.put("noise", firework.getNoise());
		values.put("type", firework.getType());
		
		writer.saveDataToFireworksTable(values);

		return firework;
	}


}

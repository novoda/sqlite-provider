package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.*;

import android.database.Cursor;

import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FireworkReader {

	private final DatabaseReader databaseWriter;

	public FireworkReader(DatabaseReader databaseWriter) {
		this.databaseWriter = databaseWriter;
	}
	
	public Firework getFirework(int primaryKey){
		Cursor cursor = databaseWriter.getFrom(TBL_FIREWORKS, primaryKey);
		
		Firework firework;
		if(cursor.moveToFirst()){
			do {
				firework = getFirework(cursor);
			} while(cursor.moveToNext());
		} else {
			Log.e("No data in the cursor. Returning null safe firework.");
			firework = Firework.getNullSafeFirework();
		}
		return firework;
	}
	
	public List<Firework> getAll(){
		Cursor cursor = databaseWriter.getAllFrom(TBL_FIREWORKS);
		
		List<Firework> fireworks = populateListWith(cursor);
		
		return fireworks;
	}
	
	private List<Firework> populateListWith(Cursor cursor) {
		List<Firework> data = new ArrayList<Firework>();
		if(cursor.moveToFirst()){
			do {
				Firework firework = getFirework(cursor);
				data.add(firework);
			} while(cursor.moveToNext());
		} else {
			Log.e("No data in the cursor.");
		}
		return data;
	}

	private Firework getFirework(Cursor cursor) {
		String name = cursor.getString(COL_IDX_NAME);
		String color = cursor.getString(COL_IDX_COLOR);
		String type = cursor.getString(COL_IDX_NOISE);
		String noise = cursor.getString(COL_IDX_TYPE);
		Firework firework = new Firework(name, color, type, noise);
		return firework;
	}
	
}

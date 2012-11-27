package com.novoda.sqliteprovider.demo.persistance;

import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_FIREWORKS;
import static com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.TBL_SHOPS;

import android.database.Cursor;

import com.novoda.sqliteprovider.demo.domain.*;
import com.novoda.sqliteprovider.demo.domain.Groups.Group;
import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.Fireworks;
import com.novoda.sqliteprovider.demo.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FireworkReader {

	private final DatabaseReader databaseReader;

	public FireworkReader(DatabaseReader databaseReader) {
		this.databaseReader = databaseReader;
	}
	
	public Firework getFirework(int primaryKey){
		Cursor cursor = databaseReader.getFrom(TBL_FIREWORKS, primaryKey);
		
		if(cursor.moveToFirst()){
			Firework firework = getFirework(cursor);
			
			cursor.close();
			
			return firework;
		} else {
			Log.e("No data in the cursor. Returning null safe firework.");
			return Firework.getNullSafeFirework();
		}
	}
	
	public List<Firework> getFireworksForShop(int primaryKey) {
		Cursor cursor = databaseReader.getChildren(TBL_SHOPS, TBL_FIREWORKS, primaryKey);
		
		List<Firework> fireworks = populateListWith(cursor);
		
		cursor.close();
		
		return fireworks;
	}
	
	public List<Firework> getFirstThreeFireworks() {
		Cursor cursor = databaseReader.getLimited(TBL_FIREWORKS, 3);
		
		List<Firework> fireworks = populateListWith(cursor);
		
		cursor.close();
		
		return fireworks;
	}
	
	public List<Firework> getUniqueFireworks() {
		Cursor cursor = databaseReader.getDistinct(TBL_FIREWORKS);
		
		List<Firework> fireworks = populateListWith(cursor);
		
		cursor.close();
		
		return fireworks;
	}
	
	public List<Firework> getAll(){
		Cursor cursor = databaseReader.getAllFrom(TBL_FIREWORKS);
		
		List<Firework> fireworks = populateListWith(cursor);
		
		cursor.close();
		
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
		String name = cursor.getString(Fireworks.COL_IDX_NAME);
		String color = cursor.getString(Fireworks.COL_IDX_COLOR);
		String type = cursor.getString(Fireworks.COL_IDX_NOISE);
		String noise = cursor.getString(Fireworks.COL_IDX_TYPE);
		double price = cursor.getDouble(Fireworks.COL_IDX_PRICE);
		Firework firework = new Firework(name, color, type, noise, price);
		return firework;
	}
	
	public Groups getCountOfRedFireworksGroupedByShop() {
		String group = Fireworks.COL_SHOP;
		String having = Fireworks.COL_COLOR +"='Red'";
		String[] selection = {"COUNT("+Fireworks.COL_COLOR+") AS count", Fireworks.COL_SHOP};
		Cursor cursor = databaseReader.getGroupedByAndHaving(TBL_FIREWORKS, group, having, selection);
		
		List<Group> data = populateGroupListWith(cursor);
		
		cursor.close();
		
		return new Groups(data);
	}
	
	private List<Group> populateGroupListWith(Cursor cursor) {
		List<Group> data = new ArrayList<Group>();
		if(cursor.moveToFirst()){
			do {
				int count = cursor.getInt(0);
				int shopId = cursor.getInt(1);
				data.add(new Group(count, shopId));
			} while(cursor.moveToNext());
		} else {
			Log.e("No data in the cursor.");
		}
		return data;
	}
}
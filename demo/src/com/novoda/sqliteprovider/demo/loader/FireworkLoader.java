package com.novoda.sqliteprovider.demo.loader;

import android.content.Context;
import android.support.v4.content.Loader;

import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.List;

public class FireworkLoader extends Loader<List<Firework>> {

	public FireworkLoader(Context context) {
		super(context);
	}

}

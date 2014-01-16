package com.novoda.sqliteprovider.demo.ui.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.List;

public class FireworkAdapter extends ArrayAdapter<Firework> {

    public FireworkAdapter(Context context, List<Firework> objects) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
    }

}

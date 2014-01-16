package com.novoda.sqliteprovider.demo.simple.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.sqliteprovider.demo.simple.R;
import com.novoda.sqliteprovider.demo.simple.provider.FireworkProvider;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportLoaderManager()
                .initLoader(R.id.loader_shop, null, new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                        return new ShopCursorLoader(getActivity());
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                        if (!cursor.moveToFirst()) {
                            Log.d("demo", "Nothing in DB, returning early");
                        }

                        do {
                            String shopName = cursor.getString(cursor.getColumnIndex("name"));
                            String shopPostcode = cursor.getString(cursor.getColumnIndex("postcode"));

                            Log.d("demo", "Found shop:" + shopName);
                            Log.d("demo", "Found shop postcode:" + shopPostcode);
                        } while (cursor.moveToNext());

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private static class ShopCursorLoader extends CursorLoader {

        public ShopCursorLoader(Context context) {
            super(context, FireworkProvider.SELECT_SHOPS, null, null, null, null);
        }
    }
}

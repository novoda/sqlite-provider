package com.novoda.sqliteprovider.demo.simple.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

    /**
     * See /assets/migrations/1_SETUP.SQL for the database creation
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * You can save multiple ways - this is just an example of using Uri's
         * do not normally do this on the UI Thread
         */
        saveNewShopToDatabase();
        /**
         * You can retrieve from the database multiple ways - this is just an example of using Uri's
         */
        retrieveFromDatabase(FireworkProvider.SHOPS, R.id.loader_shop);
        retrieveFromDatabase(FireworkProvider.CITIES, R.id.loader_city);
    }

    private void saveNewShopToDatabase() {
        Uri table = FireworkProvider.SHOPS;
        ContentValues values = new ContentValues(1);
        values.put(FireworkProvider.COL_SHOP_NAME, "MyNewShop" + System.currentTimeMillis());
        values.put(FireworkProvider.COL_SHOP_POSTCODE, "LN11YA");
        values.put(FireworkProvider.COL_SHOP_CITY_ID, 2);
        getActivity().getContentResolver().insert(table, values);
    }

    private void retrieveFromDatabase(final Uri uri, int id) {
        getActivity().getSupportLoaderManager()
                .initLoader(id, null, new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                        return new DemoCursorLoader(getActivity(), uri);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                        if (!cursor.moveToFirst()) {
                            Log.d("demo", "Nothing in DB, returning early");
                            return;
                        }

                        if (uri.equals(FireworkProvider.SHOPS)) {
                            do {
                                String shopName = cursor.getString(cursor.getColumnIndex("name"));
                                String shopPostcode = cursor.getString(cursor.getColumnIndex("postcode"));
                                String shopCityId = cursor.getString(cursor.getColumnIndex("city_id"));

                                Log.d("demo", "Found shop: " + shopName + " with postcode: " + shopPostcode + " and city_id: " + shopCityId);
                            } while (cursor.moveToNext());
                        } else {
                            do {
                                String cityName = cursor.getString(cursor.getColumnIndex("name"));
                                String cityId = cursor.getString(cursor.getColumnIndex("city_id"));

                                Log.d("demo", "Found city: " + cityName + " with city_id: " + cityId);
                            } while (cursor.moveToNext());
                        }

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        inflate.findViewById(R.id.delete_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri table = FireworkProvider.CITIES;
                getActivity().getContentResolver().delete(table, "city_id=1", null);
                retrieveFromDatabase(FireworkProvider.SHOPS, R.id.loader_shop);
                retrieveFromDatabase(FireworkProvider.CITIES, R.id.loader_city);
            }
        });
        return inflate;
    }

    private static class DemoCursorLoader extends CursorLoader {

        public DemoCursorLoader(Context context, Uri uri) {
            super(context, uri, null, null, null, null);
        }
    }
}

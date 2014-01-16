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
         */
        saveNewShopToDatabase();
        /**
         * You can retrieve from the database multiple ways - this is just an example of using Uri's
         */
        retrieveShopsFromDatabase();
    }

    private void saveNewShopToDatabase() {
        Uri table = FireworkProvider.SHOPS;
        ContentValues values = new ContentValues(1);
        values.put(FireworkProvider.COL_SHOP_NAME, "MyNewShop" + System.currentTimeMillis());
        values.put(FireworkProvider.COL_SHOP_POSTCODE, "LN11YA");
        getActivity().getContentResolver().insert(table, values);
    }

    private void retrieveShopsFromDatabase() {
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
            super(context, FireworkProvider.SHOPS, null, null, null, null);
        }
    }
}

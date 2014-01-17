package com.novoda.sqliteprovider.demo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.ui.adapter.FireworkAdapter;
import com.novoda.sqliteprovider.demo.util.Log;

public class ShopFragment extends Fragment {

    public interface OnFireworkClickListener {
        void onFireworkClick(Firework firework);
    }

    private TextView shopNameTextView;
    private TextView shopPostcodeTextView;
    private ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        shopNameTextView = (TextView) root.findViewById(R.id.activity_view_shop_name);

        shopPostcodeTextView = (TextView) root.findViewById(R.id.activity_view_shop_postcode);

        listview = (ListView) root.findViewById(android.R.id.list);

        return root;
    }

    public void setShop(Shop shop){
        if(shop != null){
            shopNameTextView.setText(shop.getName());

            shopPostcodeTextView.setText(shop.getPostcode());

            listview.setOnItemClickListener(onFireworkListItemClick);
            listview.setAdapter(new FireworkAdapter(getActivity(), shop.getFireworks()));
        } else {
            Log.e("Firework was null");
        }
    }

    private final OnItemClickListener onFireworkListItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            informActivityOfClick((Firework)listview.getItemAtPosition(position));
        }
    };

    private void informActivityOfClick(Firework firework) {
        if(getActivity() instanceof OnFireworkClickListener){
            ((OnFireworkClickListener) getActivity()).onFireworkClick(firework);
        }
    }
}

package com.novoda.sqliteprovider.demo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;
import com.novoda.sqliteprovider.demo.domain.Shop;
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.input.OnPrimaryKeyInputError;

import java.util.List;

public class FindFireworksFromOneShopFragment extends Fragment {

    public interface OnShopFound {
        void onShopFound(Shop shop);
    }

    private EditText primaryKeyEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_firework_from_one_shop, container, false);

        root.findViewById(R.id.find_firework_from_one_shop_button_find).setOnClickListener(onFindFireworkListener);
        primaryKeyEditText = (EditText) root.findViewById(R.id.find_fireworks_from_one_shop_input_shop_primary_key);

        return root;
    }

    private final OnClickListener onFindFireworkListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onFindShopFireworks();
        }
    };

    public void onFindShopFireworks(){
        if(userHasEnteredSomething()){
            try {
                int primaryKey = getPrimaryKey();

                List<Firework> fireworks = getFireworks(primaryKey);

                Shop shop = new Shop("", "Below are the Fireworks with shop primary key: "+ primaryKey, fireworks);

                informActivityShopFound(shop);
            } catch (NumberFormatException e) {
                informActivityPublicKeyInvalid();
            }
        }
    }

    private boolean userHasEnteredSomething() {
        return !TextUtils.isEmpty(primaryKeyEditText.getText());
    }

    private int getPrimaryKey() {
        return Integer.parseInt(primaryKeyEditText.getText().toString());
    }

    private List<Firework> getFireworks(int primaryKey) {
        return ((NovodaActivity) getActivity()).getApp().getFireworkReader().getFireworksForShop(primaryKey);
    }

    private void informActivityPublicKeyInvalid() {
        if(getActivity() instanceof OnPrimaryKeyInputError){
            ((OnPrimaryKeyInputError) getActivity()).onPrimaryKeyInvalid();
        }
    }

    private void informActivityShopFound(Shop shop) {
        if(getActivity() instanceof OnShopFound){
            ((OnShopFound) getActivity()).onShopFound(shop);
        }
    }
}

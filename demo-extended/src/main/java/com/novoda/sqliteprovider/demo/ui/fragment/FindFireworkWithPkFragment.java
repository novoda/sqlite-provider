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
import com.novoda.sqliteprovider.demo.ui.base.NovodaActivity;
import com.novoda.sqliteprovider.demo.ui.input.OnPrimaryKeyInputError;

public class FindFireworkWithPkFragment extends Fragment {

    public interface OnFireworkFound {
        void onFireworkFound(Firework firework);
    }

    private EditText primaryKeyEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_firework_with_pk, container, false);

        root.findViewById(R.id.find_firework_with_pk_button_find).setOnClickListener(onFindButtonClick);
        primaryKeyEditText = (EditText) root.findViewById(R.id.find_firework_with_pk_input_primary_key);

        return root;
    }

    private final OnClickListener onFindButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onFindFireworkWithPkClick();
        }
    };

    public void onFindFireworkWithPkClick() {
        if (userHasEnteredSomething()) {
            try {
                int primaryKey = getPrimaryKey();

                Firework firework = getFirework(primaryKey);

                informActivityFireworkFound(firework);
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

    private Firework getFirework(int primaryKey) {
        return ((NovodaActivity) getActivity()).getApp().getFireworkReader().getFirework(primaryKey);
    }

    private void informActivityFireworkFound(Firework firework) {
        if (getActivity() instanceof OnFireworkFound) {
            ((OnFireworkFound) getActivity()).onFireworkFound(firework);
        }
    }

    private void informActivityPublicKeyInvalid() {
        if (getActivity() instanceof OnPrimaryKeyInputError) {
            ((OnPrimaryKeyInputError) getActivity()).onPrimaryKeyInvalid();
        }
    }
}

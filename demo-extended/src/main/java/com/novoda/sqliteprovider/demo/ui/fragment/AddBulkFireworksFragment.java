package com.novoda.sqliteprovider.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.ArrayList;
import java.util.List;

public class AddBulkFireworksFragment extends Fragment {

    private EditText fireworksNumberEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_bulk_fireworks, container, false);

        fireworksNumberEditText = (EditText) root.findViewById(R.id.add_bulk_fireworks_number);
        root.findViewById(R.id.add_bulk_fireworks_add_button).setOnClickListener(onAddBulkFireworksClick);
        return root;
    }

    private final View.OnClickListener onAddBulkFireworksClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddBulkFireworksClick();
        }
    };

    private void onAddBulkFireworksClick() {
        if (fieldsArentFilledIn()) {
            notifyActivityEmptyFields();
            return;
        }

        int numberOfFireworks = Integer.valueOf(fireworksNumberEditText.getText().toString());
        List<Firework> fireworks = new ArrayList<>();
        for (int i = 0; i < numberOfFireworks; i++) {
            Firework firework = new Firework("BulkFirework", "blue", "bulk", "bang", 99.9);
            fireworks.add(firework);
        }
        notifyActivityFireworksClicked(fireworks);
        clearFields();
    }

    private void notifyActivityFireworksClicked(List<Firework> fireworks) {
        if (getActivity() instanceof AddBulkFireworksListener) {
            ((AddBulkFireworksListener) getActivity()).onBulkAddClick(fireworks);
        }
    }

    private void clearFields() {
        fireworksNumberEditText.setText("");
    }

    private void notifyActivityEmptyFields() {
        if (getActivity() instanceof AddBulkFireworksListener) {
            ((AddBulkFireworksListener) getActivity()).onEmptyInput();
        }
    }

    private boolean fieldsArentFilledIn() {
        return checkForInput(fireworksNumberEditText);
    }

    private boolean checkForInput(EditText editText) {
        return TextUtils.isEmpty(editText.getText()) || Integer.valueOf(editText.getText().toString()) == 0;
    }

    public interface AddBulkFireworksListener {
        void onEmptyInput();

        void onBulkAddClick(List<Firework> fireworks);
    }
}

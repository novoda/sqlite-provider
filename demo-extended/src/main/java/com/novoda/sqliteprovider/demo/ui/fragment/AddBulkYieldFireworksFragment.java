package com.novoda.sqliteprovider.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.novoda.sqliteprovider.demo.R;
import com.novoda.sqliteprovider.demo.domain.Firework;

import java.util.ArrayList;
import java.util.List;

public class AddBulkYieldFireworksFragment extends Fragment {

    private EditText fireworksNumberEditText;
    private EditText numberOfThreads;
    private CheckBox allowYield;
    private TextView resultLogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_bulk_yield_fireworks, container, false);

        fireworksNumberEditText = (EditText) root.findViewById(R.id.add_bulk_yield_fireworks_number);
        numberOfThreads = (EditText) root.findViewById(R.id.add_bulk_yield_fireworks_threads);
        allowYield = (CheckBox) root.findViewById(R.id.add_bulk_yield_fireworks_yield);
        resultLogs = (TextView) root.findViewById(R.id.add_bulk_yield_logs);
        root.findViewById(R.id.add_bulk_yield_fireworks_add_button).setOnClickListener(onAddBulkFireworksClick);
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
            Firework firework = new Firework("BulkYieldFirework" + i, "blue", "bulk", "bang", 99.9);
            fireworks.add(firework);
        }
        notifyActivityFireworksClicked(fireworks);
    }

    private void notifyActivityFireworksClicked(List<Firework> fireworks) {
        if (getActivity() instanceof AddBulkYieldFireworksListener) {
            ((AddBulkYieldFireworksListener) getActivity()).onBulkAddClick(
                    fireworks,
                    Integer.valueOf(numberOfThreads.getText().toString()),
                    allowYield.isChecked()
            );
        }
    }

    private void notifyActivityEmptyFields() {
        if (getActivity() instanceof AddBulkYieldFireworksListener) {
            ((AddBulkYieldFireworksListener) getActivity()).onEmptyInput();
        }
    }

    private boolean fieldsArentFilledIn() {
        return isIncorrectInput(fireworksNumberEditText) || isIncorrectInput(numberOfThreads);
    }

    private boolean isIncorrectInput(EditText editText) {
        return TextUtils.isEmpty(editText.getText()) || Integer.valueOf(editText.getText().toString()) == 0;
    }

    public void appendLog(String logs) {
        CharSequence oldLogs = resultLogs.getText();
        String text = oldLogs + "\n" + logs;
        resultLogs.setText(text);
    }

    public interface AddBulkYieldFireworksListener {
        void onEmptyInput();

        void onBulkAddClick(List<Firework> fireworks, int numberOfThreads, boolean shouldYield);
    }

}

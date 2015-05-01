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

public class AddFireworkFragment extends Fragment {

    public interface AddFireworkListener {
        void onEmptyInput();

        void onAddClick(Firework firework);
    }

    private EditText nameEditText;
    private EditText colorEditText;
    private EditText noiseEditText;
    private EditText typeEditText;
    private EditText priceEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_firework, container, false);

        nameEditText = (EditText) root.findViewById(R.id.add_firework_input_name);
        colorEditText = (EditText) root.findViewById(R.id.add_firework_input_color);
        noiseEditText = (EditText) root.findViewById(R.id.add_firework_input_noise);
        typeEditText = (EditText) root.findViewById(R.id.add_firework_input_type);
        priceEditText = (EditText) root.findViewById(R.id.add_firework_input_price);

        root.findViewById(R.id.add_firework_add_button).setOnClickListener(onAddFireworkClick);

        return root;
    }

    private final OnClickListener onAddFireworkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddFireworkClick();
        }
    };

    private void onAddFireworkClick() {
        if (fieldsArentFilledIn()) {
            notifyActivityEmptyFields();
            return;
        }

        String name = "";
        String color = "";
        String noise = "";
        String type = "";
        double price = 0;

        name = nameEditText.getText().toString();
        color = colorEditText.getText().toString();
        noise = noiseEditText.getText().toString();
        type = typeEditText.getText().toString();
        price = Double.parseDouble(priceEditText.getText().toString());

        Firework firework = new Firework(name, color, type, noise, price);

        notifyActivityFireworkClicked(firework);

        clearFields();
    }

    private boolean fieldsArentFilledIn() {
        return checkForInput(nameEditText) || checkForInput(colorEditText) || checkForInput(noiseEditText) || checkForInput(typeEditText)
                || checkForInput(priceEditText);
    }

    private boolean checkForInput(EditText editText) {
        return TextUtils.isEmpty(editText.getText());
    }

    private void notifyActivityEmptyFields() {
        if (getActivity() instanceof AddFireworkListener) {
            ((AddFireworkListener) getActivity()).onEmptyInput();
        }
    }

    private void notifyActivityFireworkClicked(Firework firework) {
        if (getActivity() instanceof AddFireworkListener) {
            ((AddFireworkListener) getActivity()).onAddClick(firework);
        }
    }

    private void clearFields() {
        nameEditText.setText("");
        colorEditText.setText("");
        noiseEditText.setText("");
        typeEditText.setText("");
        priceEditText.setText("");
    }
}

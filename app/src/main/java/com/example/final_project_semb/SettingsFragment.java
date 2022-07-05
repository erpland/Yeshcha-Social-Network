package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    ViewGroup root;
    SwitchMaterial[] switches = new SwitchMaterial[6];
    PreferencesManager preferencesManager;
    SettingsManager settingsManager;
    ImageView logout_btn;

    @Override
    public void onClick(View v) {
        settingsManager.logOut();
    }

    public interface SettingsManager{
        void updatePreferences(View v,PreferencesManager preferencesManager);
        void logOut();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        settingsManager = (SettingsManager) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, null);
        preferencesManager = getArguments().getParcelable("settingsParcel");

        Spinner spinnerLanguages = root.findViewById(R.id.spinner_distance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.distance_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);

        initValues();
        setSwitches();
        logout_btn.setOnClickListener(this);
        return root;
    }

    private void setSwitches() {
        ArrayList<Boolean> activeStates = preferencesManager.GetArrayOfActiveState();
        for (int i = 0; i < switches.length; i++) {
            switches[i].setChecked(activeStates.get(i));
            switches[i].setOnCheckedChangeListener(this);
        }
    }

    private void initValues() {
        for (int i = 0; i < switches.length; i++) {
            int resID = getResources().getIdentifier("switch" + (i + 1), "id", root.getContext().getPackageName());
            switches[i] = root.findViewById(resID);
        }
        logout_btn = root.findViewById(R.id.iv_logout);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.switch1:
                preferencesManager.basicEqt.setActive(compoundButton.isChecked());
                break;
            case R.id.switch2:
                preferencesManager.computerMobilEqt.setActive(compoundButton.isChecked());
                break;
            case R.id.switch3:
                preferencesManager.officeEqt.setActive(compoundButton.isChecked());
                break;
            case R.id.switch4:
                preferencesManager.othersEqt.setActive(compoundButton.isChecked());
                break;
            case R.id.switch5:
                preferencesManager.personalHygieneEqt.setActive(compoundButton.isChecked());
                break;
            case R.id.switch6:
                preferencesManager.petEqt.setActive(compoundButton.isChecked());
                break;


        }
        settingsManager.updatePreferences(compoundButton,preferencesManager);
    }
}
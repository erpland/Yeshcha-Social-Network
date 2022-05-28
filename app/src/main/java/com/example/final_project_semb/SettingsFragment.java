package com.example.final_project_semb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SettingsFragment extends Fragment {
    ViewGroup root;
    SwitchMaterial[] switches = new SwitchMaterial[6];
    PreferencesManager preferencesManager;

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
        return root;
    }

    private void setSwitches() {
        ArrayList<Boolean> activeStates = preferencesManager.GetArrayOfActiveState();
        for (int i = 0; i < switches.length; i++) {
            switches[i].setChecked(activeStates.get(i));
        }
    }

    private void initValues() {
        for (int i = 0; i < switches.length; i++) {
            int resID = getResources().getIdentifier("switch" + (i + 1), "id", root.getContext().getPackageName());
            switches[i] = root.findViewById(resID);
        }
    }
}
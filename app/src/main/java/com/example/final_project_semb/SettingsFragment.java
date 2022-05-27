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
    Map<String, Object> preferences = new HashMap<>();
    SwitchMaterial[] switches = new SwitchMaterial[6];
    BasicProductsEqt basicEqt;
    ComputerAndMobileEqt computerMobilEqt;
    OfficeEqt officeEqt;
    PersonalHygieneEqt personalHygieneEqt;
    PetEqt petEqt;
    OthersEqt othersEqt;
    ArrayList<PreferencesManager>preferencesManagers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, null);
        Spinner spinnerLanguages = root.findViewById(R.id.spinner_distance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.distance_options, android.R.layout.simple_spinner_item);

        preferences = (Map<String, Object>) getArguments().getSerializable("settingParcel");
        initValues();
//        setSwitches();
        Toast.makeText(root.getContext(), preferences.get("1").toString(), Toast.LENGTH_LONG).show();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);
        return root;
    }

    private void setSwitches() {
    for (int i=0;i<switches.length;i++){
        switches[i].setChecked(preferencesManagers.get(i+1).getIsActive());
    }
    }

    private void initValues() {
        switches[0] = root.findViewById(R.id.switch1);
        switches[1] = root.findViewById(R.id.switch2);
        switches[2] = root.findViewById(R.id.switch3);
        switches[3] = root.findViewById(R.id.switch4);
        switches[4] = root.findViewById(R.id.switch5);
        switches[5] = root.findViewById(R.id.switch6);
        // TODO: 27/05/2022 figure out how to cast hash map objects 
//        basicEqt = new BasicProductsEqt((BasicProductsEqt) preferences.get("1").;

//        computerMobilEqt =new ComputerAndMobileEqt ((ComputerAndMobileEqt) preferences.get("2"));
//        officeEqt = new OfficeEqt((OfficeEqt) preferences.get("3"));
//        othersEqt = new OthersEqt((OthersEqt) preferences.get("4"));
//        personalHygieneEqt =new PersonalHygieneEqt ((PersonalHygieneEqt) preferences.get("5"));
//        petEqt = new PetEqt((PetEqt) preferences.get("6"));
//        preferencesManagers=new ArrayList<>(){
//            {add(basicEqt);add(computerMobilEqt);add(officeEqt);add(othersEqt);add(personalHygieneEqt);add(petEqt);
//            }
//        };

    }
}
package com.example.final_project_semb;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

public class OpenPostFragment extends Fragment implements View.OnClickListener {
    MapView map;
    ViewGroup root;
    ConstraintLayout openPostOverlay_cl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup)inflater.inflate(R.layout.fragment_open_post, container, false);
        openPostOverlay_cl = root.findViewById(R.id.cl_openPostOverlay);
        openPostOverlay_cl.setOnClickListener(this);
        map = root.findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        return root;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(root.getContext(),"close",Toast.LENGTH_SHORT).show();
    }
}
package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewPostFragment extends Fragment implements View.OnClickListener {
ViewGroup root;
CardView newPostCardView;
ConstraintLayout newPostOverlay;
FragmentHandler fragmentHandler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_new_post, container, false);
        initViews();
        newPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        newPostOverlay.setOnClickListener(this);
        return root;
    }

    private void initViews() {
        newPostOverlay = root.findViewById(R.id.newPostOverlay);
        newPostCardView = root.findViewById(R.id.newPostCardView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newPostOverlay:
                fragmentHandler.closeAllFragment();
        }
    }
}
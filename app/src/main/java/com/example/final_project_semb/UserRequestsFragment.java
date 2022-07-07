package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class UserRequestsFragment extends Fragment implements View.OnClickListener {
    ViewGroup root;
    ListView requestsListView;
    ArrayList<Post>posts;
    ConstraintLayout overlay;
    FragmentHandler fragmentHandler;
    ConstraintLayout inner;
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_user_requests, container, false);
        posts= getArguments().getParcelableArrayList("postParcel");
        initView();
        RequestAdapter adapter=new RequestAdapter(root.getContext(),0,posts);
        requestsListView.setAdapter(adapter);
        overlay.setOnClickListener(this);
        inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

    private void initView() {
        requestsListView=root.findViewById(R.id.userRequestList);
        overlay=root.findViewById(R.id.cl_requestPostOverlay);
        inner=root.findViewById(R.id.cl_requestPostInner);
    }


    @Override
    public void onClick(View view) {
        fragmentHandler.closeAllFragment(this);
    }
}
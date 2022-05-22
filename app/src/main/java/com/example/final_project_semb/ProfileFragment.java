package com.example.final_project_semb;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    User user;
    ViewGroup root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        user = (User)getArguments().getSerializable("userKey");
        root = (ViewGroup) inflater.inflate(R.layout.fragment_register_page2, null);
//        Toast.makeText(root.getContext(),"user name:"+user.getEmail(),Toast.LENGTH_LONG).show();
        return root;

    }
}
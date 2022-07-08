package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView lstPost;
    ViewGroup root;
    ArrayList<Post> posts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        posts = getArguments().getParcelableArrayList("postParcel");
        initView();
        lstPost.setAdapter(new PostAdapter(root.getContext(), posts));
        lstPost.setLayoutManager(new LinearLayoutManager(root.getContext()));
        return root;
    }



    private void initView() {
        lstPost = (RecyclerView) root.findViewById(R.id.lst_posts);
    }
}
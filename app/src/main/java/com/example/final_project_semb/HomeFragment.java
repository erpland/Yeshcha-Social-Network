package com.example.final_project_semb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView lstPost;
    ViewGroup root;
    ArrayList<Post> posts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        try {
            posts = getArguments().getParcelableArrayList("postParcel");
            PostAdapter adapter = new PostAdapter(root.getContext(), 0, posts);
            initView();
            lstPost.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(root.getContext(),"error " + e,Toast.LENGTH_SHORT).show();
        }
        Log.d("posts","aaa");

        return root;
    }

    private void initView() {
        lstPost = root.findViewById(R.id.lst_posts);
    }
}
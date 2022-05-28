package com.example.final_project_semb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class PostsFragment extends Fragment {
    ListView lstPost;
    ViewGroup root;
    ArrayList<Post> posts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_posts, null);
        posts = getArguments().getParcelable("postsParcelable");
        PostAdapter adapter = new PostAdapter(root.getContext(), 0,posts);
        initView();
        lstPost.setAdapter(adapter);
        return root;
    }

    private void initView() {
        lstPost = root.findViewById(R.id.lst_posts);
    }
}
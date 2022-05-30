package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView lstPost;
    ViewGroup root;
    ArrayList<Post> posts;
    PostCallback postCallback;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        postCallback = (PostCallback) context;
    }
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

            lstPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(root.getContext(), "Click " + view.getId(), Toast.LENGTH_SHORT).show();
                    postCallback.getClickedPost(view,posts.get(0));
                }
            });
        }catch(Exception e){
            Toast.makeText(root.getContext(),"error " + e,Toast.LENGTH_SHORT).show();
        }
        Log.d("posts","aaa");

        return root;
    }
    public interface PostCallback{
        public void getClickedPost(View id,Post post);
    }



    private void initView() {
        lstPost = root.findViewById(R.id.lst_posts);
    }
}
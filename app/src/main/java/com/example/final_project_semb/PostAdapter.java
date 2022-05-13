package com.example.final_project_semb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private ArrayList<Post> arr;

    public PostAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.post_layout, null);
        }
        if (arr.size() > 0) {
            Post p = arr.get(position);
            ImageView imgPost = convertView.findViewById(R.id.userImg);
            TextView title = convertView.findViewById(R.id.title);
            TextView body = convertView.findViewById(R.id.body);
            TextView name = convertView.findViewById(R.id.userName);
            TextView location = convertView.findViewById(R.id.location);
            imgPost.setImageResource(p.userImage);
            title.setText(p.title);
            body.setText(p.body);
            name.setText(p.name);
            location.setText(p.location);

        }
        return convertView;
    }
}
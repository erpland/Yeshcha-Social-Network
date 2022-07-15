package com.example.final_project_semb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends ArrayAdapter<Post>{
    private Context context;
    private ArrayList<Post> arr;
    FragmentsCallbacks fragmentsCallbacks;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arr = new ArrayList<>(objects);
        fragmentsCallbacks=(FragmentsCallbacks) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.private_posts_layout, null);
        }
        if (arr.size() > 0) {
            Post p = arr.get(position);
            TextView title = convertView.findViewById(R.id.privatePostTitle);
            TextView body = convertView.findViewById(R.id.privatePostBody);
            TextView date  =convertView.findViewById(R.id.privatePostDate);
            Button accept = convertView.findViewById(R.id.privatePostAccept);
            Button delete = convertView.findViewById(R.id.privatePostDelete);

            title.setText(p.title);
            body.setText(p.body);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-mm-yyyy");
            String strDate = dateFormat.format(p.date);
            date.setText(strDate);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentsCallbacks.deactivatePost(p,position);

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentsCallbacks.deletePost(p,position);
                }
            });
        }
        return convertView;
    }
}

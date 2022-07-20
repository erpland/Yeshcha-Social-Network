package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> arr;
    private FragmentsCallbacks fragmentsCallbacks;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    byte[] imgBytes = new byte[64];
    Bitmap bmp;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false));
    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.arr = new ArrayList<>(posts);
        bmp=BitmapFactory.decodeResource(context.getResources(),R.drawable.green_noise);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fragmentsCallbacks = (FragmentsCallbacks) context;
        Post p = arr.get(position);
        getBitMapedImage(p.user.image,holder);
        holder.title.setText("ישך " + p.title + "?!");
        holder.body.setText(p.body);
        holder.name.setText(p.user.name);
        holder.location.setText("" + p.distanceFromUser);
        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentsCallbacks.getClickedUser(holder.itemView,p.user);
            }
        });
        holder.postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentsCallbacks.getClickedPost(holder.itemView, p);
            }
        });
    }

    public int getItemCount() {
        return this.arr.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, body, name, location;
        private ImageView imgPost;
        private View postView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.postView = view.findViewById(R.id.post_arrow);
            this.imgPost = view.findViewById(R.id.userImg);
            this.title = view.findViewById(R.id.title);
            this.body = view.findViewById(R.id.body);
            this.name = view.findViewById(R.id.userName);
            this.location = view.findViewById(R.id.location);
        }
    }

    private void getBitMapedImage(String url,ViewHolder holder) {
        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(url);
        final long FIVE_MEGA = 1024 * 1024*5;
        httpsReference.getBytes(FIVE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgPost.setImageBitmap(Bitmap.createBitmap(bmp));
                Log.d("bmp", "onSuccsess: " + bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("bmp", "onFailure: ");
            }
        });
    }
}
package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    User user;
    ImageView profilePic;
    TextView name, phone;
    RatingBar stars;
    ViewGroup root;
    StorageReference storageRef;
    FirebaseStorage firebaseStorage;
    Button editProfileBtn, showPostsBtn;
    PrivateProfileHBtnHandler privateProfileHBtnHandler;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        privateProfileHBtnHandler = (PrivateProfileHBtnHandler) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);
        user = (User) getArguments().getParcelable("userParcel");
        initViews();
        initVars();
        showPostsBtn.setOnClickListener(this);
        editProfileBtn.setOnClickListener(this);
        setData();
        return root;
    }

    private void setData() {
        name.setText(user.getName());
        phone.setText(user.getPhoneNumber());
        setProfileImage();
        stars.setRating(user.getFlow_level());
    }

    private void initVars() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
    }

    public void setProfileImage() {
        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(user.image);

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(Bitmap.createBitmap(bmp));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void initViews() {
        profilePic = root.findViewById(R.id.publicProfile_picture);
        name = root.findViewById(R.id.profile_name);
        phone = root.findViewById(R.id.profile_phone);
        stars = root.findViewById(R.id.rb_publicProfileStars);
        editProfileBtn = root.findViewById(R.id.btn_editProfile);
        showPostsBtn = root.findViewById(R.id.btn_showPosts);
    }
    public interface PrivateProfileHBtnHandler{
        void openEditProfile();
        void openPrivatePosts();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_editProfile:
                privateProfileHBtnHandler.openEditProfile();
                break;
            case R.id.btn_showPosts:
                privateProfileHBtnHandler.openPrivatePosts();
                break;
        }
    }
}
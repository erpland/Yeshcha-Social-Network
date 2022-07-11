package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    ViewGroup root;
    ImageView[] startsArr;
    StorageReference storageRef;
    FirebaseStorage firebaseStorage;
    Button editProfileBtn, showPostsBtn;
    PrivateProfileHBtnHandler privateProfileHBtnHandler;
    boolean hasPosts;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        privateProfileHBtnHandler = (PrivateProfileHBtnHandler) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);
        user = (User) getArguments().getParcelable("userParcel");
        hasPosts = getArguments().getBoolean("hasPosts");
        initViews();
        initVars();
//        showPostsBtn.setEnabled(hasPosts);

        showPostsBtn.setOnClickListener(this);
        editProfileBtn.setOnClickListener(this);
        setData();
        return root;
    }

    private void setData() {
        name.setText(user.getName());
        phone.setText(user.getPhoneNumber());
        setProfileImage();
        Log.d("startprofile", user.getFlow_level() + "");
        for (int i = 0; i < user.getFlow_level() && i < 5; i++) {
            startsArr[i].setColorFilter(ContextCompat.getColor(root.getContext(), R.color.sub_headline));
        }
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
        editProfileBtn = root.findViewById(R.id.btn_editProfile);
        showPostsBtn = root.findViewById(R.id.btn_showPosts);
        startsArr = new ImageView[5];
        startsArr[0] = root.findViewById(R.id.iv_star1_private);
        startsArr[1] = root.findViewById(R.id.iv_star2_private);
        startsArr[2] = root.findViewById(R.id.iv_star3_private);
        startsArr[3] = root.findViewById(R.id.iv_star4_private);
        startsArr[4] = root.findViewById(R.id.iv_star5_private);
    }

    public interface PrivateProfileHBtnHandler {
        void openEditProfile();

        void openPrivatePosts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editProfile:
                privateProfileHBtnHandler.openEditProfile();
                break;
            case R.id.btn_showPosts:
                privateProfileHBtnHandler.openPrivatePosts();
                break;
        }
    }
}
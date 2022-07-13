package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PublicProfileFragment extends Fragment implements View.OnClickListener {
    ViewGroup root;
    User user;
    ImageView profilePic;
    TextView name;
    ImageView [] startsArr;
    FirebaseStorage firebaseStorage;
    FragmentHandler fragmentHandler;
    ConstraintLayout publicProfileOverlay;
    CardView userCard;
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_public_profile, null);
        user = (User) getArguments().getParcelable("userParcel");
        initViews();
        initVars();
        setData();
        publicProfileOverlay.setOnClickListener(this);
        //און קליק ריק כדי שלחיצה על הכרטיס לא תסגור אותו
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}});
        return root;
    }

    private void setData() {
        name.setText(user.getName());
        setProfileImage();
        Log.d("publicprofilestars",user.getFlow_level() + "");
        for(int i=0;i<user.getFlow_level() && i < 5;i++){
            startsArr[i].setColorFilter(ContextCompat.getColor(root.getContext(), R.color.sub_headline));
        }
    }

    private void initVars() {
        firebaseStorage = FirebaseStorage.getInstance();
    }

    private void initViews() {
        profilePic = root.findViewById(R.id.publicProfile_picture);
        name = root.findViewById(R.id.publicUserName);
        startsArr = new ImageView[5];
        startsArr[0] = root.findViewById(R.id.iv_star1);
        startsArr[1] = root.findViewById(R.id.iv_star2);
        startsArr[2] = root.findViewById(R.id.iv_star3);
        startsArr[3] = root.findViewById(R.id.iv_star4);
        startsArr[4] = root.findViewById(R.id.iv_star5);
        publicProfileOverlay = root.findViewById(R.id.cl_publicPostOverlay);
        userCard = root.findViewById(R.id.cv_userCardView);
    }

    public void setProfileImage() {
        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(user.image);

        final long ONE_MEGABYTE = 1024 * 1024*5;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_publicPostOverlay:
                fragmentHandler.closeAllFragment();
                break;
        }
    }
}
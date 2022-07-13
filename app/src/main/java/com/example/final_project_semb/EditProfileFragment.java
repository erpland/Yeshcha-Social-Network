package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    ViewGroup root;
    User user;
    ImageView profilePic;
    EditText name,phone;
    Button update,close;
    ConstraintLayout overlay;
    CardView card;
    StorageReference storageRef;
    FirebaseStorage firebaseStorage;
    FragmentHandler fragmentHandler;
    EditUserHandler editUserHandler;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
        editUserHandler = (EditUserHandler) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_edit_profile, container, false);
        user = (User) getArguments().getParcelable("userParcel");
        initViews();
        initVars();
        initData();
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        overlay.setOnClickListener(this);
        update.setOnClickListener(this);
        close.setOnClickListener(this);
        profilePic.setOnClickListener(this);

        return root;
    }

    private void initVars() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference("Users");
    }

    private void initData() {
        name.setText(user.getName());
        phone.setText(user.getPhoneNumber());
        setProfileImage();

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

    private void initViews() {
        profilePic = root.findViewById(R.id.editProfilePic);
        name = root.findViewById(R.id.et_editName);
        phone = root.findViewById(R.id.et_editPhone);
        update =root.findViewById(R.id.btn_editSave);
        close = root.findViewById(R.id.btn_editClose);
        overlay = root.findViewById(R.id.cl_editProfileOverlay);
        card  = root.findViewById(R.id.cv_editUserCardView);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_editClose:
            case R.id.cl_editProfileOverlay:
                fragmentHandler.closeAllFragment();
                break;
            case R.id.btn_editSave:
                String nameStr = name.getText().toString();
                String phoneStr = phone.getText().toString();
                editUserHandler.updateUser(nameStr,phoneStr);
                break;
            case R.id.editProfilePic:
                editUserHandler.updateImage(v.getId());
                break;

        }
    }
    public interface EditUserHandler{
        void updateUser(String name, String phoneNumber);
        void updateImage(int viewId);
    }
}
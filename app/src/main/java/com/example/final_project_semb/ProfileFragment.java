package com.example.final_project_semb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    User user;
    ImageView profilePic;
    TextView name, phone;
    RatingBar stars;
    ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);
        user = (User) getArguments().getParcelable("userParcel");
        initValues();
        name.setText(user.getName());
        phone.setText(user.getPhoneNumber());
        // TODO: 27/05/2022 ask roie what to do with profile picture-maybe async?
//        profilePic.setImageURI(Uri.parse(user.getImage()));
        stars.setRating(user.getFlow_level());
        return root;


    }

    private void initValues() {
        profilePic = root.findViewById(R.id.profile_picture);
        name = root.findViewById(R.id.profile_name);
        phone = root.findViewById(R.id.profile_phone);
        stars = root.findViewById(R.id.rb_profileStars);

    }

}
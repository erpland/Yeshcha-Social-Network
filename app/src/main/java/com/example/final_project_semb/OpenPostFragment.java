package com.example.final_project_semb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.google.type.LatLng;

public class OpenPostFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    MapView mapView;
    ViewGroup root;

    Button close_btn, chat_btn;
    Post post;
    FirebaseStorage firebaseStorage;
    TextView title, body, name, location;
    ImageView profile_pic;
    FragmentHandler fragmentHandler;
    CardView postCardView;
    ConstraintLayout openPostOverlay;
    OpenPostInterface openPostInterface;
    private GoogleMap mMap;

    @Override

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
        openPostInterface = (OpenPostInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_open_post, container, false);
        post = (Post) getArguments().getParcelable("post");
        initViews();
        initVars();
        initData();
        close_btn.setOnClickListener(this);
        postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        openPostOverlay.setOnClickListener(this);
        chat_btn.setOnClickListener(this);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);


        return root;
    }


    private void initData() {
        name.setText(post.user.getName());
        body.setText(post.getBody());
        title.setText(post.getTitle());
        location.setText(Double.toString(post.getDistanceFromUser()));
        setProfileImage();


    }

    public interface OpenPostInterface {
        void openChat(View view, String phoneNumber);
    }

    private void initViews() {
        mapView = root.findViewById(R.id.mapView);
        close_btn = root.findViewById(R.id.btn_closePostFrmnt);
        postCardView = root.findViewById(R.id.cv_postCardView);
        openPostOverlay = root.findViewById(R.id.cl_openPostOverlay);
        name = root.findViewById(R.id.openPostUserName);
        title = root.findViewById(R.id.openPostTitle);
        body = root.findViewById(R.id.openPostBody);
        profile_pic = root.findViewById(R.id.openPostProfilePic);
        chat_btn = root.findViewById(R.id.chatButton);
        location = root.findViewById(R.id.openPostLocation);
    }

    public void setProfileImage() {
        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(post.user.getImage());

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile_pic.setImageBitmap(Bitmap.createBitmap(bmp));
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
            case R.id.btn_closePostFrmnt:
            case R.id.cl_openPostOverlay:
                fragmentHandler.closeAllFragment(this);
                break;
            case R.id.chatButton:
                openPostInterface.openChat(v,post.user.getPhoneNumber());
                break;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(post.getLat(), post.getLng());
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("אני פה"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f));
    }

    private void initVars() {
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
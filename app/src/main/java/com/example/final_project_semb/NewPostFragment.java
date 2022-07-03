package com.example.final_project_semb;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewPostFragment extends Fragment implements View.OnClickListener {
ViewGroup root;
Button close_btn,add_btn;
EditText title,body;
TextView location;
CardView newPostCardView;
ConstraintLayout newPostOverlay;
FragmentHandler fragmentHandler;
User user;
Location currentLocation;
AddPostInterface addPostInterface;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
        addPostInterface=(AddPostInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_new_post, container, false);
        user=(User)getArguments().getParcelable("userParcel");
        currentLocation=getArguments().getParcelable("currentLocation");
        initViews();
        initData();
        newPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        newPostOverlay.setOnClickListener(this);
        close_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        return root;
    }
    public interface AddPostInterface {
        void addNewPost(View view, String title, String body, Location location, Date date);
    }
    private void initViews() {
        newPostOverlay = root.findViewById(R.id.newPostOverlay);
        newPostCardView = root.findViewById(R.id.newPostCardView);
        close_btn=root.findViewById(R.id.new_post_cancel);
        add_btn=root.findViewById(R.id.new_post_add);
        title=root.findViewById(R.id.new_post_title);
        body=root.findViewById(R.id.new_post_body);
        location=root.findViewById(R.id.new_post_location);
    }
    private void initData(){
    location.setText(getLocation());

    }
    private String getLocation(){
        String strAdd = "";
        Geocoder geocoder = new Geocoder(root.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("My Current location address", strReturnedAddress.toString());
            } else {
                Log.w("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Canont get Address!");
        }
        return strAdd;
    }
    private boolean validateInputs(){
        return !title.getText().toString().isEmpty()&&!body.getText().toString().isEmpty()&&
                title.getText().toString().length()<=30&&body.getText().toString().length()<=100;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_post_cancel:
            case R.id.newPostOverlay:
                fragmentHandler.closeAllFragment(this);
                break;
            case R.id.new_post_add:
                    if (validateInputs()){
                        addPostInterface.addNewPost(v,title.getText().toString(),body.getText().toString(),currentLocation,Calendar.getInstance().getTime());
                    }
                    else{
                        Toast.makeText(root.getContext(),"לא ניתן להוסיף פוסט ריק ו/אחד או יותר מהשדות לא תקין",Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }
}
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewPostFragment extends Fragment implements View.OnClickListener {
    ViewGroup root;
    Button close_btn, add_btn;
    EditText title, body;
    TextView location;
    CardView newPostCardView;
    ConstraintLayout newPostOverlay;
    FragmentHandler fragmentHandler;
    User user;
    Location currentLocation;
    AddPostInterface addPostInterface;
    Spinner spinnerCategory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHandler = (FragmentHandler) context;
        addPostInterface = (AddPostInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_new_post, container, false);
        user = (User) getArguments().getParcelable("userParcel");
        currentLocation = getArguments().getParcelable("currentLocation");
        initViews();
        initData();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCategory.setAdapter(adapter);

        newPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("item_spinner", spinnerCategory.getSelectedItemPosition() +"");
            }
        });
        newPostOverlay.setOnClickListener(this);
        close_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        return root;
    }

    public interface AddPostInterface {
        void addNewPost(View view, String title, String body, Location location, Date date,int categoryCode);
    }

    private void initViews() {
        newPostOverlay = root.findViewById(R.id.newPostOverlay);
        newPostCardView = root.findViewById(R.id.newPostCardView);
        close_btn = root.findViewById(R.id.new_post_cancel);
        add_btn = root.findViewById(R.id.new_post_add);
        title = root.findViewById(R.id.new_post_title);
        body = root.findViewById(R.id.new_post_body);
        location = root.findViewById(R.id.new_post_location);
        spinnerCategory = root.findViewById(R.id.spinner_category);
    }

    private void initData() {
        location.setText(getLocation());

    }

    private String getLocation() {
        String str = "";
        Geocoder geocoder = new Geocoder(root.getContext(), Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(
                    currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            Address address = addressList.get(0);
            str = address.getLocality().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }



    private boolean validateInputs() {
        return !title.getText().toString().isEmpty() && !body.getText().toString().isEmpty() &&
                title.getText().toString().length() <= 30 && body.getText().toString().length() <= 100;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_post_cancel:
            case R.id.newPostOverlay:
                fragmentHandler.popNavBackStack();
                break;
            case R.id.new_post_add:
                if (validateInputs()) {
                    addPostInterface.addNewPost(v, title.getText().toString(), body.getText().toString(), currentLocation, Calendar.getInstance().getTime(),spinnerCategory.getSelectedItemPosition());
                } else {
                    Toast.makeText(root.getContext(), "לא ניתן להוסיף פוסט ריק ו/אחד או יותר מהשדות לא תקין", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
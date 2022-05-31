package com.example.final_project_semb;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, HomeFragment.PostCallback, OpenPostFragment.OpenPostCallback {
    BottomNavigationView bottomNavigation_ly;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference userDocument;
    User user = null;
    Reply reply = null;
    Requests requests = null;
    Map<String, Object> preferences = new HashMap<>();
    NavController navController;
    PreferencesManager preferencesManager;
    BottomNavigationView bottomNavigationView;
    FrameLayout postsHost;
    private FusedLocationProviderClient fusedLocationClient;
    Location currentLocation;
    private static final int LOCATION_CODE = 44;

    ArrayList<Post> list = new ArrayList<>(); // demo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        demoData();
        initVars();
        initViews();
        initUser();
        initReplies();
        initPreferences();
        initRequests();
        initNavbar();

        bottomNavigationView.setOnItemSelectedListener(this);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (user != null && reply != null && requests != null && preferencesManager != null && currentLocation !=null) {
                    closeLoader();
                    timer.cancel();
                }
            }
        }, 0, 3000);

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        Log.d("locationtest", location.getLatitude() + " " + location.getLongitude());
//                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                        List<Address> addressList = null;
//                        try {
//                            addressList = geocoder.getFromLocation(
//                                    location.getLatitude(), location.getLongitude(), 1);
//                            Address address = addressList.get(0);
//                            Log.d("locationtest", "onLocationResult: " + address.getLocality() + address.getPostalCode() + address.getCountryName());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void askLocationPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                                getCurrentLocation();
                            } else {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_CODE);
                            }
                        }
                );


// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
                else{
                    new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                            .setTitle("בעיה...")
                            .setMessage("לא אישרת הרשאת גישה למיקום שלך לכן לא ניתן להמשיך")
                            .setNegativeButton("אני אפס...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAffinity();
                                    System.exit(0);
                                }
                            })
                            .setPositiveButton("אוקי אני בוא ננסה שוב", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recreate();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        }
    }

    private void closeLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("postParcel", (ArrayList<? extends Parcelable>) list);
                bottomNavigationView.setVisibility(View.VISIBLE);
                Navigation.findNavController(MainActivity.this, R.id.activity_main_nav_host_fragment).navigate(R.id.homeFragment, bundle);
            }
        });


    }

    private void demoData() {
        list.add(new Post("שלומי ואן סטפן", "ישך ריזלה?!", "מחפש ריזלה דחוף", R.drawable.user1, "חיפה"));
        list.add(new Post("סמי עופר", "ישך סובארו אימפרזה?!", "מחפש סובארו אימפרזה טורבו לשוד", R.drawable.user2, "רופין"));
        list.add(new Post("דליה רביץ", "ישך מחשב נייד?!", "מחפשת נייד לפרוץ לפנטגון", R.drawable.user3, "אשדוד"));
        list.add(new Post("מוטי אבנר", "ישך מחק?!", "מחפש מחק למחוק משהו", R.drawable.user4, "כפר סבא"));
        list.add(new Post("דודו אהרון", "ישך סיר לחץ?!", "מחפש סיר לחץ לבישול בשר בקר", R.drawable.user5, "הרצליה"));
        list.add(new Post("הדר בירקנשטוק", "ישך פקק?!", "מחפש מחק פקק לבקבוק קוקה קולה", R.drawable.user6, "כוכב יאיר"));
    }

    private void initVars() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void initViews() {
        postsHost = findViewById(R.id.fl_postsHost);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
    }

    private void initNavbar() {
        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void initUser() {
        userDocument = db.collection("users").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(User.class);
                    } else {
                        Log.d("tag1", "userNotExist", task.getException());
                    }
                } else {
                    Log.d("tag1", "userNotSucess ", task.getException());
                }
            }
        });
    }

    private void initReplies() {
        userDocument = db.collection("Replies").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        reply = document.toObject(Reply.class);
                    } else {
                        Log.d("tag1", "get failed with Replies", task.getException());
                    }
                } else {
                    Log.d("tag1", "RepliesNotFound", task.getException());
                }
            }
        });
    }

    private void initRequests() {
        userDocument = db.collection("Requests").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        requests = document.toObject(Requests.class);
                    } else {
                        Log.d("tag1", "get failed with Requests ", task.getException());
                    }
                } else {
                    Log.d("tag1", "RequestsNotFound", task.getException());
                }
            }
        });
    }

    private void initPreferences() {
        userDocument = db.collection("Preferences").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("test1", document.getData() + "");
                        preferencesManager = document.toObject(PreferencesManager.class);
                    } else {
                        Log.d("tag1", "get failed with Preferences ", task.getException());
                    }
                } else {
                    Log.d("tag1", "PreferencesNotFOUND ", task.getException());
                }
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.homeFragment:
                bundle.putParcelableArrayList("postParcel", (ArrayList<? extends Parcelable>) list);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.homeFragment, bundle);
                break;
            case R.id.profileFragment:
                bundle.putParcelable("userParcel", user);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.profileFragment, bundle);
                break;
            case R.id.settingsFragment:
                bundle.putParcelable("settingsParcel", preferencesManager);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.settingsFragment, bundle);
                break;
        }
        return true;
    }

    @Override
    public void getClickedPost(View id, Post post) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_postsHost, new OpenPostFragment(), null)
                .setReorderingAllowed(true)
                .addToBackStack("openPostFragment") // name can be null
                .commit();
    }

    @Override
    public void closeOpenPostFragment() {
        getSupportFragmentManager().popBackStack("openPostFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
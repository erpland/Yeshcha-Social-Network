package com.example.final_project_semb;

import static android.widget.Toast.LENGTH_LONG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.google.firebase.firestore.FieldValue.arrayRemove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, PostAdapter.PostCallback, FragmentHandler, OpenPostFragment.OpenPostInterface, NewPostFragment.AddPostInterface, SettingsFragment.SettingsManager, ProfileFragment.PrivateProfileHBtnHandler, EditProfileFragment.EditUserHandler, RequestAdapter.PrivatePostHandler, HomeFragment.RefreshHandler {
    BottomNavigationView bottomNavigation_ly;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DocumentReference userDocument, postsDocument;
    ArrayList<Post> posts;
    User user = null;
    Reply reply = null;
    Post post = null;
    Requests requests = null;
    Map<String, Object> preferences = new HashMap<>();
    NavController navController;
    PreferencesManager preferencesManager;
    BottomNavigationView bottomNavigationView;
    FrameLayout postsHost;
    private final static int GALLERY_PHOTO = 99;
    Uri updatedImageUri;
    ImageView editProfileImg;
    int editProfileImageId;
    private FusedLocationProviderClient fusedLocationClient;
    Location currentLocation;
    private static final int LOCATION_CODE = 44;
    FrameLayout postsHost_fl;
    Fragment openPostFragment;
    Fragment newPostFragment;
    Fragment publicProfileFragment;
    Fragment editProfileFragment;
    Fragment userRequestFragment;
    View host;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

            if (navController.getCurrentDestination().getId() == R.id.homeFragment) {
                logOut();
            } else if (navController.getCurrentDestination().getId() == R.id.loaderFragment) {
                return;
            } else {
                navController.popBackStack();
            }
        } else {
            closeFragments(getVisibleFragment());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initVars();
        initViews();
        initReplies();
        initRequests();
        initNavbar();

        bottomNavigationView.setOnItemSelectedListener(this);

        Timer timer = new Timer();
        CountDownTimer count = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom)
                        .setTitle("בעיה...")
                        .setMessage("לא הצלחנו לקבל את הנתונים,נסה שוב עם הבעיה ממשיכה אנה צור קשר איתנו")
                        .setPositiveButton("נסה שוב", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                            }
                        }).setNegativeButton("סגירה", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.exit(0);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
        count.start();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (user != null && reply != null && requests != null && preferencesManager != null && currentLocation != null) {
                    Collections.sort(posts, new Comparator<Post>() {
                        @Override
                        public int compare(Post o1, Post o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                    closeLoader();
                    count.cancel();
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
                    }
                }
                if (user == null) {
                    initUser();
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
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    new AlertDialog.Builder(this, R.style.AlertDialogCustom)
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
                bundle.putParcelableArrayList("postParcel", (ArrayList<? extends Parcelable>) posts);
                bottomNavigationView.setVisibility(View.VISIBLE);
                Navigation.findNavController(MainActivity.this, R.id.activity_main_nav_host_fragment).navigate(R.id.homeFragment, bundle);
            }
        });


    }

    private void initVars() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Users");
    }

    private void initViews() {
        postsHost = findViewById(R.id.fl_postsHost);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        openPostFragment = new OpenPostFragment();
        newPostFragment = new NewPostFragment();
        publicProfileFragment = new PublicProfileFragment();
        editProfileFragment = new EditProfileFragment();
        userRequestFragment = new UserRequestsFragment();
        postsHost_fl = findViewById(R.id.fl_postsHost);

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
                        initPreferences();
                    } else {
                        Log.d("tag1", "userNotExist", task.getException());
                    }
                } else {
                    Log.d("tag1", "userNotSucess ", task.getException());
                }

            }
        });

    }


    private void initPosts() {
        posts = new ArrayList<>();
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                post = document.toObject(Post.class);
                                if (isPostFit(post) && post.getIsActive()) {
                                    post.convertLatLngToString(calcDistanceFromUser(post.getLat(), post.getLng()));
                                    getUserForPost(post);
                                }

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private boolean isPostFit(Post post) {
        switch (post.getCategoryCode()) {
            case 0:
                return preferencesManager.basicEqt.getActive();

            case 1:
                return preferencesManager.computerMobilEqt.getActive();

            case 2:
                return preferencesManager.officeEqt.getActive();

            case 3:
                return preferencesManager.othersEqt.getActive();

            case 4:
                return preferencesManager.personalHygieneEqt.getActive();

            case 5:
                return preferencesManager.petEqt.getActive();

        }
        return false;
    }

    private void getUserForPost(Post post) {
        DocumentReference docRef = db.collection("users").document(post.userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();
                    if (userDoc != null) {
                        post.setUser(userDoc.toObject(User.class));
                        posts.add(post);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
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
                    Map<String, Object> postMap = document.getData();
//                    ArrayList<Post>exampleArr= (ArrayList<Post>)(postMap).get("postList");

                    if (document.exists()) {
                        requests = document.toObject(Requests.class);
//                        requests.setPosts(exampleArr);

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
                        initPosts();

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
        closeFragments(getVisibleFragment());
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.homeFragment:
                bundle.putParcelableArrayList("postParcel", (ArrayList<? extends Parcelable>) posts);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.homeFragment, bundle);
                openLoaderOnUpdate();
                break;
            case R.id.profileFragment:
                bundle.putParcelable("userParcel", user);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.profileFragment, bundle);
                break;
            case R.id.settingsFragment:
                bundle.putParcelable("settingsParcel", preferencesManager);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.settingsFragment, bundle);
                break;
            case R.id.newPostFragment:
                bundle.putParcelable("userParcel", user);
                bundle.putParcelable("currentLocation", currentLocation);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.newPostFragment, bundle);
        }
        return true;
    }

    @Override
    public void getClickedPost(View id, Post post) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);
        openPostFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_postsHost, openPostFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("openPostFragment") // name can be null
                .commit();
    }

    @Override
    public void getClickedUser(View id, User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("userParcel", user);
        publicProfileFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_postsHost, publicProfileFragment, null)
                .setReorderingAllowed(true)

                .addToBackStack("publicProfile") // name can be null
                .commit();
    }

    @Override
    public void closeAllFragment(Fragment f) {
//        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void popNavBackStack() {
        navController.popBackStack();
    }

    public double calcDistanceFromUser(double lat, double lng) {
        Location endPoint = new Location("post");
        endPoint.setLatitude(lat);
        endPoint.setLongitude(lng);
        return currentLocation.distanceTo(endPoint);
    }

    //why?
    public void closeFragments(Fragment f) {
        if (f != null)
            getSupportFragmentManager().beginTransaction().remove(f).commit();
    }

    public Fragment getVisibleFragment() {
        if (openPostFragment.isVisible())
            return openPostFragment;
        else if (newPostFragment.isVisible())
            return newPostFragment;
        else if (publicProfileFragment.isVisible())
            return publicProfileFragment;
        return null;
    }

    @Override
    public void replyOnPost(View view, String phoneNumber) {
        reply.increaseReplyAmount();
        db.collection("Replies").document(mAuth.getUid()).set(reply);
        user.setFlow_level(reply.getReplyAmount());
        db.collection("users").document(mAuth.getUid()).update("flow_level", user.getFlow_level());
        openChat(phoneNumber);
        closeAllFragment(openPostFragment);
        openLoaderOnUpdate();

    }

    private void openChat(String phoneNumber) {
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+972" + "0549828502" + "&text=" + "Hi there!!!"));
            startActivity(intent);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+972" + phoneNumber, null, "אשמח לעזור!!", null, null);
            Toast.makeText(this, "הודעת אס אם אס נשלחה ליעד", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLoaderOnUpdate() {
        Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.loaderFragment);
        recreate();
    }

    //Create method appInstalledOrNot
    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void addNewPost(View view, String title, String body, Location location, Date date, int categoryCode) {
        Post post = new Post(body, date, location.getLatitude(), location.getLongitude(), title, mAuth.getUid(), categoryCode);
        addPostToFireStore(post);
        requests.increaseRequestAmount();
        requests.addPosts(post);
        db.collection("Requests").document(mAuth.getUid()).set(requests);
        openLoaderOnUpdate();

    }


    private void addPostToFireStore(Post post) {
        // Add a new document with a generated ID
        db.collection("Posts").document(mAuth.getUid() + "$$" + System.currentTimeMillis())
                .set(post);
        getSupportFragmentManager().popBackStack("openPostFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);


    }


    @Override
    public void updatePreferences(View v, PreferencesManager pManager) {
        preferencesManager = pManager;
        db.collection("Preferences").document(mAuth.getUid()).set(preferencesManager);
    }

    @Override
    public void logOut() {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("עצור!")
                .setMessage("אתה בטוח שאתה רוצה להתנתק?!")
                .setNegativeButton("לא לא תשאיר פה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("כן תעיף אותי מפה", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, StartActivity.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void openEditProfile() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("userParcel", user);
        editProfileFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_postsHost, editProfileFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("editProfile")
                .commit();
    }

    @Override
    public void openPrivatePosts() {
        Bundle bundle = new Bundle();
        ArrayList<Post> filteredList = filterPostList(requests.getPosts());
        bundle.putParcelableArrayList("postParcel", filteredList);
        userRequestFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_postsHost, userRequestFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("userPostList")
                .commit();
    }

    private ArrayList<Post> filterPostList(ArrayList<Post> list) {
        ArrayList<Post> filteredList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsActive()) {
                filteredList.add(list.get(i));
            }
        }
        return filteredList;
    }

    @Override
    public void updateUser(String name, String phoneNumber) {
        if (!ValidateUpdatedInput(name, phoneNumber)) return;
        if (name != user.getName()) {
            user.setName(name);
            db.collection("users").document(mAuth.getUid()).update("name", user.getName());
        }
        if (phoneNumber != user.getPhoneNumber()) {
            user.setPhoneNumber(phoneNumber);
            db.collection("users").document(mAuth.getUid()).update("phoneNumber", user.getPhoneNumber());
        }
        if (editProfileImg != null) {
            uploadImage();
        } else if (editProfileImg == null && (name != user.getName() || phoneNumber != user.getPhoneNumber())) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("userParcel", user);
            navController.navigate(R.id.profileFragment, bundle);
        }
        closeFragments(editProfileFragment);


    }

    public void uploadImage() {
        final StorageReference ref = storageReference.child(mAuth.getUid()).child("Profile_" + System.currentTimeMillis() + ".jpg");
        UploadTask imageUpload = ref.putFile(updatedImageUri);
        Task<Uri> urlTask = imageUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    updatedImageUri = task.getResult();
                    user.setImage(updatedImageUri.toString());
                    db.collection("users").document(mAuth.getUid()).update("image", user.getImage());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("userParcel", user);
                    navController.navigate(R.id.profileFragment, bundle);
                } else {
                    Toast.makeText(MainActivity.this, "בעיה בתמונה.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ValidateUpdatedInput(String name, String phoneNumber) {
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "אחד או יותר מהשדות ריקים", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() > 10) {
            Toast.makeText(this, "שם יכול להכיל עד 10 אותיות", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneNumber.matches("^(?:(?:(\\+?972|\\(\\+?972\\)|\\+?\\(972\\))(?:\\s|\\.|-)?([1-9]\\d?))|(0[23489]{1})|(0[57]{1}[0-9]))(?:\\s|\\.|-)?([^0\\D]{1}\\d{2}(?:\\s|\\.|-)?\\d{4})$")) {
            Toast.makeText(this, "מספר טלפון לא תקין", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void updateImage(int viewId, int imageView) {
        editProfileImageId = imageView;
        takeGalleryAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    updatedImageUri = data.getData();
                    editProfileImg = findViewById(editProfileImageId);
                    editProfileImg.setImageURI(updatedImageUri);

                } else {
                    Toast.makeText(this, "Operation failed", LENGTH_LONG).show();
                }
                break;

        }
    }

    //open gallery
    private void takeGalleryAction() {
        Toast.makeText(this, "take gallery action", LENGTH_LONG).show();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_PHOTO);
    }


    @Override
    public void deactivePost(Post p, int position) {
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayRemove(p));
        requests.posts.get(position).setIsActive(false);
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayUnion(requests.posts.get(position)));
        db.collection("Posts").document(mAuth.getUid() + "$$" + p.getDate().getTime()).update("isActive", false);

    }

    @Override
    public void deletePost(Post p, int position) {
        db.collection("Posts").document(mAuth.getUid() + "$$" + p.getDate().getTime()).delete();
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayRemove(p));

    }



    @Override
    public void refreshPage() {
      recreate();
    }
}
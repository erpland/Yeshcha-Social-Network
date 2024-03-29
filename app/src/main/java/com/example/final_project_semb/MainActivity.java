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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, FragmentsCallbacks {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DocumentReference userDocument;
    ArrayList<Post> posts;
    User user = null;
    Reply reply = null;
    Post post = null;
    Requests requests = null;
    PreferencesManager preferencesManager;

    NavController navController;
    BottomNavigationView bottomNavigationView;
    FrameLayout postsHost;

    private final static int GALLERY_PHOTO = 99;
    Uri updatedImageUri;
    ImageView editProfileImg;
    int editProfileImageId;
    FusedLocationProviderClient fusedLocationClient;
    Location currentLocation;
    private static final int LOCATION_CODE = 44;

    boolean IS_LOCATION_READY;
    boolean IS_USER_READY;
    boolean IS_POSTS_READY;
    boolean IS_REQUESTS_READY;
    boolean IS_REPLIES_READY;
    boolean IS_PREFERENCES_READY;
    boolean IS_FIRST_RUN = true;
    boolean IS_IMAGE_CHANGED;
    boolean IS_GPS_ON;

    Fragment openPostFragment;
    Fragment newPostFragment;
    Fragment publicProfileFragment;
    Fragment editProfileFragment;
    Fragment userRequestFragment;

    CountDownTimer errorCountdownTimer;
    Timer isEverythingLoadedTimer;


    // ניהול לחיצה אחורה - ניווט נכון או התנתקות
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
            closeAllFragment();
        }
    }
    //התמדדות עם חזרה מהקפאה של אפלקצייה  וכן חזרה מאינטנט של הגלרייה
    @Override
    protected void onResume() {
        super.onResume();
        if (!IS_FIRST_RUN && !IS_IMAGE_CHANGED) {
            openLoaderOnUpdate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        IS_GPS_ON = isGpsEnabled();
        initVars();
        initViews();
        initReplies();
        initRequests();
        initNavbar();
        bottomNavigationView.setOnItemSelectedListener(this);

        loadingErrorCountdown();
        isEverythingLoadedInterval();
    }
    // בדיקה שכל הרכיבים הדרושים נטענו - כדי לעבור לרשימת הפוסטים
    private void isEverythingLoadedInterval() {
        isEverythingLoadedTimer = new Timer();
        isEverythingLoadedTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (IS_LOCATION_READY && IS_USER_READY && IS_PREFERENCES_READY && IS_REPLIES_READY && IS_REQUESTS_READY && IS_POSTS_READY && IS_GPS_ON) {
                    IS_FIRST_RUN = false;
                    posts.sort(new Comparator<Post>() {
                        @Override
                        public int compare(Post o1, Post o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                    closeLoader();
                    errorCountdownTimer.cancel();
                    isEverythingLoadedTimer.cancel();
                }
            }
        }, 0, 1500);
    }
    // מניעת הצגת מסך טעינה אין סופית בעת בעייה בקבלת הרכיבים הדרושים
    private void loadingErrorCountdown() {
        errorCountdownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom)
                        .setTitle("בעיה...")
                        .setMessage("לא הצלחנו לקבל את הנתונים,נסה שוב עם הבעיה ממשיכה אנא צור קשר איתנו")
                        .setPositiveButton("נסה שוב", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refreshPage();
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
        errorCountdownTimer.start();
    }
    // פונקצייה עזר לוידוא מיקום דלוק
    public boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isEnabled)
            return true;
        else {
            new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                    .setTitle("בעיה...")
                    .setMessage("הפעל את אפשרות המיקום כדי להמשיך")
                    .setNegativeButton("לא יודע איך", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setPositiveButton("הפעלתי", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isGpsEnabled();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return false;
    }
    // קבלת מיקום נוכחי
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        isGpsEnabled();
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(MainActivity.this, "מיקום לא מזוהה", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        IS_LOCATION_READY = true;
                    }
                }
                if (user == null) {
                    initUser();
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
    // בקשת הראשאות מיקום
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
    // התמודדות עם תגובה לבקשת מיקום
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
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
                        .setPositiveButton("לא יודע איך...", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                startActivity(i);
                                refreshPage();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
    // סגירת מסך הטעינה והעברה למסך הפוסטים
    private void closeLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("postParcel", posts);
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

    }
    //איתחול תפריט הניווט
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
                        IS_USER_READY = true;
                        initPreferences();
                    } else {
                        Log.d("tag1", "userNotExist", task.getException());
                    }
                } else {
                    Log.d("tag1", "userNotSuccess ", task.getException());
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
    //בדיקה שפוסט עומד בהגדרות המשתמש
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
    //משיכת יוזר מתאים לטובת הצגת פרופיל ציבורי בפוסט בהצלבה
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
                IS_POSTS_READY = true;
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
                IS_REPLIES_READY = true;
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
                IS_REQUESTS_READY = true;
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
                        preferencesManager = document.toObject(PreferencesManager.class);
                        initPosts();
                    } else {
                        Log.d("tag1", "get failed with Preferences ", task.getException());
                    }
                } else {
                    Log.d("tag1", "PreferencesNotFOUND ", task.getException());
                }
                IS_PREFERENCES_READY = true;
            }
        });

    }
    //ניהול אירועי לחיצה על עצמים של תפריט הניווט
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //סגירת כל הפרגמנטים הקטנים למקרה שיש פתוחים
        closeAllFragment();
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.homeFragment:
                bundle.putParcelableArrayList("postParcel", posts);
                Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.homeFragment, bundle);
                openLoaderOnUpdate();
                break;
            case R.id.profileFragment:
                bundle.putParcelable("userParcel", user);
//                bundle.putBoolean("hasPosts", requests.posts.size() != 0);
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
                .addToBackStack("modalFragments") // name can be null
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
                .addToBackStack("modalFragments") // name can be null
                .commit();
    }

    @Override
    public void closeAllFragment() {
        getSupportFragmentManager().popBackStack("modalFragments", androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    @Override
    public void replyOnPost(View view, String phoneNumber, String title) {
        reply.increaseReplyAmount();
        db.collection("Replies").document(mAuth.getUid()).set(reply);
        user.setFlowLevelByReplies(reply.getReplyAmount());
        db.collection("users").document(mAuth.getUid()).update("flow_level", user.getFlow_level());
        openChat(phoneNumber, title);
        closeAllFragment();
//        openLoaderOnUpdate();

    }
    //פתיחת וואסטאפ למשתמש הרצוי עם הודעת פתיחה
    private void openChat(String phoneNumber, String title) {
        String msg = "אהלן, יש לי " + title + "...";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/972" + phoneNumber + "?text=" + msg));
        startActivity(intent);
    }
    // מעבר חזרה למסך טעינה ואיתחול מחדש של האקטיביטי
    private void openLoaderOnUpdate() {
        Navigation.findNavController(this, R.id.activity_main_nav_host_fragment).navigate(R.id.loaderFragment);
        refreshPage();
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
    // הוספת פוסט חדש לפיירסטור
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
                .addToBackStack("modalFragments")
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
                .addToBackStack("modalFragments")
                .commit();
    }
    // פילטור הפוסטים הפעילים לטבות הצגתם
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
        if (!name.equals(user.getName())) {
            user.setName(name);
            db.collection("users").document(mAuth.getUid()).update("name", user.getName());
        }
        if (!phoneNumber.equals(user.getPhoneNumber())) {
            user.setPhoneNumber(phoneNumber);
            db.collection("users").document(mAuth.getUid()).update("phoneNumber", user.getPhoneNumber());
        }
        if (editProfileImg != null) {
            uploadImage();
        }
        closeAllFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("userParcel", user);
        navController.navigate(R.id.profileFragment, bundle);
        IS_IMAGE_CHANGED = false;
    }
    // העלת תמונת משתמש חדשה
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
    // ולידציה לעדכון פרטי משתמש
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
    public void updateImage(int viewId) {
        editProfileImageId = viewId;
        takeGalleryAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PHOTO) {
            IS_IMAGE_CHANGED = true;
            if (resultCode == RESULT_OK) {
                updatedImageUri = data.getData();
                editProfileImg = findViewById(editProfileImageId);
                editProfileImg.setImageURI(updatedImageUri);
            } else {
                Toast.makeText(this, "טעינת תמונה נכשלה", LENGTH_LONG).show();
            }
        }
    }
    // פתיחת הגלרייה
    private void takeGalleryAction() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_PHOTO);
    }

    @Override
    public void deactivatePost(Post p, int position) {
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayRemove(p));
        requests.posts.get(position).setIsActive(false);
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayUnion(requests.posts.get(position)));
        db.collection("Posts").document(mAuth.getUid() + "$$" + p.getDate().getTime()).update("isActive", false);
        closeAllFragment();
    }

    @Override
    public void deletePost(Post p, int position) {
        posts.remove(p);
        requests.posts.remove(p);
        db.collection("Posts").document(mAuth.getUid() + "$$" + p.getDate().getTime()).delete();
        db.collection("Requests").document(mAuth.getUid()).update("posts", FieldValue.arrayRemove(p));
        closeAllFragment();
    }

    @Override
    public void refreshPage() {
        recreate();
    }
}



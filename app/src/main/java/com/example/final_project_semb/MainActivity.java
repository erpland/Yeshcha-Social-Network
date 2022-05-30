package com.example.final_project_semb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, HomeFragment.PostCallback {
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


    ArrayList<Post> list = new ArrayList<>(); // demo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        demoData();
        initVars();
        initUser();
        initReplies();
        initPreferences();
        initRequests();

        //DEMO!


        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(this);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if(user!=null && reply != null && requests !=null && preferencesManager !=null ) {
                        closeLoader();

                        timer.cancel();

                }
            }
        },0,3000);

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
        FrameLayout postsHost = findViewById(R.id.fl_postsHost);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fl_postsHost, new OpenPostFragment(), null)
                .setReorderingAllowed(true)
                .addToBackStack("f1") // name can be null
                .commit();
    }
}
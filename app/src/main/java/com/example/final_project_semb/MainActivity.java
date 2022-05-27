package com.example.final_project_semb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView lstPosts;
    BottomNavigationView bottomNavigation_ly;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference userDocument;
    User user=null;
    Reply reply=null;
    Requests requests=null;
    Map<String,Object>preferences=new HashMap<>();

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstPosts = findViewById(R.id.lst_posts);
        initVars();
        initUser();
        initReplies();
        initPreferences();
        initRequests();
        ArrayList<Post> list = new ArrayList<>();

        list.add(new Post("שלומי ואן סטפן","ישך ריזלה?!","מחפש ריזלה דחוף",R.drawable.user1,"חיפה"));
        list.add(new Post("סמי עופר","ישך סובארו אימפרזה?!","מחפש סובארו אימפרזה טורבו לשוד",R.drawable.user2,"רופין"));
        list.add(new Post("דליה רביץ","ישך מחשב נייד?!","מחפשת נייד לפרוץ לפנטגון",R.drawable.user3,"אשדוד"));
        list.add(new Post("מוטי אבנר","ישך מחק?!","מחפש מחק למחוק משהו",R.drawable.user4,"כפר סבא"));
        list.add(new Post("דודו אהרון","ישך סיר לחץ?!","מחפש סיר לחץ לבישול בשר בקר",R.drawable.user5,"הרצליה"));
        list.add(new Post("הדר בירקנשטוק","ישך פקק?!","מחפש מחק פקק לבקבוק קוקה קולה",R.drawable.user6,"כוכב יאיר"));

        PostAdapter adapter = new PostAdapter(this,0,list);
        lstPosts.setAdapter(adapter);

        bottomNavigation_ly = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this,R.id.fl_content);
        NavigationUI.setupWithNavController(bottomNavigation_ly,navController);
        bottomNavigation_ly.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.settings_page:
                    Fragment settingsFragment=new SettingsFragment();
                    bundle=new Bundle();
                    bundle.putSerializable("settingParcel", (Serializable) preferences);
                    settingsFragment.setArguments(bundle);
                    replaceFragment(settingsFragment);
                    break;
                case R.id.profile_page:
                    Fragment profile=new ProfileFragment();
                   bundle=new Bundle();
                    bundle.putParcelable("userParcel",user);
                    profile.setArguments(bundle);
                    replaceFragment(profile);
                    break;
            }
            return true;
        });

    }
    private void initVars(){
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();


    }

    private void initUser(){
        userDocument=db.collection("users").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user=document.toObject(User.class);
                    } else {
                        Log.d("tag1", "userNotExist", task.getException());
                    }
                } else {
                    Log.d("tag1", "userNotSucess ", task.getException());
                }
            }
        });
    }



    private void initReplies(){
        userDocument=db.collection("Replies").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        reply=document.toObject(Reply.class);
                    } else {
                        Log.d("tag1", "get failed with Replies", task.getException());
                    }
                } else {
                    Log.d("tag1", "RepliesNotFound", task.getException());
                }
            }
        });
    }
    private void initRequests(){
        userDocument=db.collection("Requests").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        requests=document.toObject(Requests.class);
                    } else {
                        Log.d("tag1", "get failed with Requests ", task.getException());
                    }
                } else {
                    Log.d("tag1", "RequestsNotFound", task.getException());
                }
            }
        });
    }
    private void initPreferences(){
        userDocument=db.collection("Preferences").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        preferences=document.getData();
                    } else {
                        Log.d("tag1", "get failed with Preferences ", task.getException());
                    }
                } else {
                    Log.d("tag1", "PreferencesNotFOUND ", task.getException());
                }
            }
        });
    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setCustomAnimations(R.anim.slide_to_right,R.anim.slide_to_left,R.anim.slide_from_right,R.anim.slide_from_left)
                .replace(R.id.fl_content, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();

    }
}
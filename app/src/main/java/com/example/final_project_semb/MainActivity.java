package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lstPosts;
    BottomNavigationView bottomNavigation_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstPosts = findViewById(R.id.lst_posts);
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
        bottomNavigation_ly.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.settings_page:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setCustomAnimations(R.anim.slide_to_right,R.anim.slide_to_left,R.anim.slide_from_right,R.anim.slide_from_left)
                .replace(R.id.fl_content, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();

    }
}
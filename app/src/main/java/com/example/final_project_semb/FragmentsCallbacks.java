package com.example.final_project_semb;


import android.location.Location;
import android.view.View;
import java.util.Date;

public interface FragmentsCallbacks {
    void closeAllFragment();
    void popNavBackStack();
    void deactivatePost(Post p, int position);
    void deletePost(Post p,int position);
    void addNewPost(View view, String title, String body, Location location, Date date, int categoryCode);
    void updateUser(String name, String phoneNumber);
    void updateImage(int viewId);
    void refreshPage();
    void openEditProfile();
    void openPrivatePosts();
    void updatePreferences(View v,PreferencesManager preferencesManager);
    void replyOnPost(View view, String phoneNumber,String title);
    void getClickedPost(View id, Post post);
    void getClickedUser(View id,User user);
    void logOut();
}

package com.example.final_project_semb;


import android.graphics.Bitmap;

import java.util.ArrayList;

public class User {
    int flow_level;
    String email;
    Bitmap image;
    String name;
    String phoneNumber;
    ArrayList preferences;
    ArrayList replies;
    ArrayList requests;

    public User(String email, Bitmap image, String name, String phoneNumber) {
        this.flow_level = 0;
        this.email = email;
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = new ArrayList();
        this.replies = new ArrayList();
        this.requests = new ArrayList();
    }
    public User(){

    }

    public int getFlow_level() {
        return flow_level;
    }

    public void setFlow_level(int flow_level) {
        this.flow_level = flow_level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList preferences) {
        this.preferences = preferences;
    }

    public ArrayList getReplies() {
        return replies;
    }

    public void setReplies(ArrayList replies) {
        this.replies = replies;
    }

    public ArrayList getRequests() {
        return requests;
    }

    public void setRequests(ArrayList requests) {
        this.requests = requests;
    }
}

package com.example.final_project_semb;


import java.util.ArrayList;

public class User {
    int flow_level;
    String email;
    String image;
    String name;
    int phoneNumber;
    ArrayList preferences;
    ArrayList replies;
    ArrayList requests;

    public User(int flow_level, String email, String image, String name, int phoneNumber, ArrayList preferences, ArrayList replies, ArrayList requests) {
        this.flow_level = flow_level;
        this.email = email;
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = preferences;
        this.replies = replies;
        this.requests = requests;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
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

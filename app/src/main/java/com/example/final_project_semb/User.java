package com.example.final_project_semb;


import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    int flow_level;
    String email;
    String image;
    String name;
    String phoneNumber;


    public User(String email, String image, String name, String phoneNumber) {
        this.flow_level = 0;
        this.email = email;
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

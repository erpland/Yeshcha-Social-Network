package com.example.final_project_semb;

import android.location.Location;

import java.util.Date;

public class Post {
    String name,title,body,location;
    int userImage;
    User user;
    boolean isActive;
    Date date;
    Location geoLocation;

    public Post(String name, String title, String body, int userImage,String location) {
        this.name = name;
        this.title = title;
        this.body = body;
        this.userImage = userImage;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getLocation() { return location; }
}

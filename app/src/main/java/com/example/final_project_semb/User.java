package com.example.final_project_semb;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Parcelable {
    int flow_level;
    String email;
    String image;
    String name;
    String phoneNumber;


    public User(String email, String image, String name, String phoneNumber) {
        this.flow_level = 1;
        this.email = email;
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;

    }
    public User(){

    }

    protected User(Parcel in) {
        flow_level = in.readInt();
        email = in.readString();
        image = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getFlow_level() {
        return flow_level;
    }
    public void setFlow_level(int flow_level) { this.flow_level = flow_level;}

    public void setFlowLevelByReplies(int replies) {
        if (replies<=2){
            this.flow_level=1;
        }
        else if(replies<=6){
            this.flow_level=2;
        }
        else if(replies<=10){
            this.flow_level=3;
        }
        else if(replies<=15){
            this.flow_level=4;
        }
        else if(replies<=20){
            this.flow_level=5;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(flow_level);
        parcel.writeString(email);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
    }
}

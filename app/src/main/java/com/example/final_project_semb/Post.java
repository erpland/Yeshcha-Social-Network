package com.example.final_project_semb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class Post implements Parcelable {
    String name,title,body,location,phoneNumber;
    String image;
    User user;
    boolean isActive;
    Date date;
    Location geoLocation;


public Post(){}
    public Post(String body, Date date, Location geoLocation, String image,boolean isActive,String name,String phoneNumber,String title) {
        this.name = name;
        this.title = title;
        this.body = body;
        this.image = image;
        this.date=date;
        this.isActive=isActive;
        this.geoLocation = geoLocation;
        this.phoneNumber=phoneNumber;

    }



    protected Post(Parcel in) {
        name = in.readString();
        title = in.readString();
        body = in.readString();
        image = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        isActive = in.readByte() != 0;
        geoLocation = in.readParcelable(Location.class.getClassLoader());
        phoneNumber=in.readString();

    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() { return location; }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(image);
        dest.writeParcelable(user, flags);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeParcelable(geoLocation, flags);
    }
}

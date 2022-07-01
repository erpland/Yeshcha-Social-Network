package com.example.final_project_semb;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Post implements Parcelable {
    String title, body, location;
    String userUid;
    boolean isActive;
    Date date;
    User user;
    Location geoLocation;


    public Post() {
    }

    public Post(String body, Date date, Location geoLocation, boolean isActive, String title) {

        this.title = title;
        this.body = body;

        this.date = date;
        this.isActive = isActive;
        this.geoLocation = geoLocation;


    }


    protected Post(Parcel in) {

        title = in.readString();
        body = in.readString();

        userUid = in.readString();
        isActive = in.readByte() != 0;
        user = in.readParcelable(User.class.getClassLoader());
        geoLocation = in.readParcelable(Location.class.getClassLoader());


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


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }


    public String getLocation() {
        return location;
    }


    public String getUserUid() {
        return userUid;
    }

    public User getUser() {
        return user;
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


    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(userUid);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeParcelable(geoLocation, flags);
        dest.writeParcelable(user, flags);
    }
}
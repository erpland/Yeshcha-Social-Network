package com.example.final_project_semb;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.type.LatLng;

import java.util.Date;

public class Post implements Parcelable {
    String title, body;
    String userUid;
    boolean isActive;
    Date date;
    User user;
//    LatLng geoLocation;
    double distanceFromUser;

    double lat,lng;

    public Post() {
    }
    public double getDistanceFromUser() {return distanceFromUser;}

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }
    public Post(String body, Date date, double lat,double lng, String title, String userUid) {

        this.title = title;
        this.body = body;
        this.date = date;
        this.isActive = true;
        this.lat=lat;
        this.lng=lng;
        this.userUid=userUid;
    }


    protected Post(Parcel in) {
        title = in.readString();
        body = in.readString();
        userUid = in.readString();
        isActive = in.readByte() != 0;
        user = in.readParcelable(User.class.getClassLoader());
//        geoLocation = in.readParcelable(LatLng.class.getClassLoader());
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
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }



//    public LatLng getGeoLocation(){return geoLocation;}

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

//    public void setGeoLocation(LatLng geoLocation) {
//        this.geoLocation = geoLocation;
//    }

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
//        dest.writeParcelable(geoLocation, flags);
        dest.writeParcelable(user, flags);
    }
}
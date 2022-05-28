package com.example.final_project_semb;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Post implements Parcelable {
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

    protected Post(Parcel in) {
        name = in.readString();
        title = in.readString();
        body = in.readString();
        location = in.readString();
        userImage = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        isActive = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(location);
        dest.writeInt(userImage);
        dest.writeParcelable(user, flags);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeParcelable(geoLocation, flags);
    }
}

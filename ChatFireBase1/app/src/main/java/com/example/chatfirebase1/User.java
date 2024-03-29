package com.example.chatfirebase1;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String uuid;
    private String username;
    private String profileUrl;

    public User() {
    }

    public User(String uuid, String username, String profileUrl) {
        this.uuid = uuid;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    protected User(Parcel in) {
        uuid = in.readString();
        username = in.readString();
        profileUrl = in.readString();
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

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(username);
        parcel.writeString(profileUrl);
    }
}

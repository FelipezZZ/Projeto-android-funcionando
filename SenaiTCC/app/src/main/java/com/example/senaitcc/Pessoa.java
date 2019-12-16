package com.example.senaitcc;

import android.os.Parcel;
import android.os.Parcelable;

public class Pessoa implements Parcelable {

    private String uuid;
    private String fbnome;
    private String profileUrl;
    private int fbcod_pessoa;

    public Pessoa() {
    }

    public Pessoa(String uuid, String fbnome, String profileUrl, int fbcod_pessoa) {
        this.uuid = uuid;
        this.fbnome = fbnome;
        this.profileUrl = profileUrl;
        this.fbcod_pessoa = fbcod_pessoa;
    }

    protected Pessoa(Parcel in) {
        uuid = in.readString();
        fbnome = in.readString();
        profileUrl = in.readString();
        fbcod_pessoa = in.readInt();
    }

    public static final Creator<Pessoa> CREATOR = new Creator<Pessoa>() {
        @Override
        public Pessoa createFromParcel(Parcel in) {
            return new Pessoa(in);
        }

        @Override
        public Pessoa[] newArray(int size) {
            return new Pessoa[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public String getFbnome() {
        return fbnome;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public int getFbcod_pessoa() {
        return fbcod_pessoa;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(fbnome);
        parcel.writeString(profileUrl);
        parcel.writeInt(fbcod_pessoa);
    }
}

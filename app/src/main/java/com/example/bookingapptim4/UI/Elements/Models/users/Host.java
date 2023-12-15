package com.example.bookingapptim4.UI.Elements.Models.users;

import android.os.Parcel;
import android.os.Parcelable;

public class Host implements Parcelable {

    private Long id;
    private String email;
    private String jwt;

    public Host(Long id, String email, String jwt) {
        this.id = id;
        this.email = email;
        this.jwt = jwt;
    }

    protected Host(Parcel in) {
        id = in.readLong();
        email = in.readString();
        jwt = in.readString();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getJwt() {
        return jwt;
    }

    public static final Creator<Host> CREATOR = new Creator<Host>() {
        @Override
        public Host createFromParcel(Parcel in) {
            return new Host(in);
        }

        @Override
        public Host[] newArray(int size) {
            return new Host[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(email);
        dest.writeString(jwt);
    }
}

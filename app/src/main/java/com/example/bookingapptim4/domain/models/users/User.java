package com.example.bookingapptim4.domain.models.users;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private Long id;

    private String password;

    private UserStatus status = UserStatus.Inactive;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private Role role = Role.GUEST;

    private String jwt;

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        password = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        jwt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(jwt);
    }

    @Override
    public int describeContents() {
        return 0;
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
}

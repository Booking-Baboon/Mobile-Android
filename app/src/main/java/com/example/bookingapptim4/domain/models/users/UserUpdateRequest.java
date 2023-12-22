package com.example.bookingapptim4.domain.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserUpdateRequest implements Parcelable {
    private Long id;

    private String email;
    private String firstName;

    private String lastName;
    private String address;
    private String phoneNumber;
    private String jwt;

    public UserUpdateRequest() {

    }

    public UserUpdateRequest(Long id, String email, String firstName, String lastName, String address, String phoneNumber, String jwt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.jwt = jwt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    protected UserUpdateRequest(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        jwt = in.readString();
    }

    public static final Creator<UserUpdateRequest> CREATOR = new Creator<UserUpdateRequest>() {
        @Override
        public UserUpdateRequest createFromParcel(Parcel in) {
            return new UserUpdateRequest(in);
        }

        @Override
        public UserUpdateRequest[] newArray(int size) {
            return new UserUpdateRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(jwt);
    }
}

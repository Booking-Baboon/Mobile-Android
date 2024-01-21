package com.example.bookingapptim4.domain.models.accommodations;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private String country;
    private String city;
    private String address;

    public Location(String country, String city, String address) {
        this.country = country;
        this.city = city;
        this.address = address;
    }

    protected Location(Parcel in) {
        country = in.readString();
        city = in.readString();
        address = in.readString();
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(address);
    }
}

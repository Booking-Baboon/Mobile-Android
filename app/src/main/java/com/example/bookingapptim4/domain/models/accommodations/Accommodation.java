package com.example.bookingapptim4.domain.models.accommodations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Accommodation implements Parcelable {

    private String Name;
    private String Description;
    private String Location;
    private boolean PricingPerPerson;
    private int MinGuests;
    private int MaxGuests;
    private List<String> Amenities;
    private String HostUsername;
    private int Price;

    public Accommodation(String name, String description, String location, int price) {
        Name = name;
        Description = description;
        Location = location;
        Price = price;
    }

    public Accommodation(String name, String description, String location, boolean pricingPerPerson, int minGuests, int maxGuests, List<String> amenities, String hostUsername, int price) {
        Name = name;
        Description = description;
        Location = location;
        PricingPerPerson = pricingPerPerson;
        MinGuests = minGuests;
        MaxGuests = maxGuests;
        Amenities = amenities;
        HostUsername = hostUsername;
        Price = price;
    }

    protected Accommodation(Parcel in) {
        Name = in.readString();
        Description = in.readString();
        Location = in.readString();
        Price =Integer.getInteger(in.readString()) ;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public boolean isPricingPerPerson() {
        return PricingPerPerson;
    }

    public void setPricingPerPerson(boolean pricingPerPerson) {
        PricingPerPerson = pricingPerPerson;
    }

    public int getMinGuests() {
        return MinGuests;
    }

    public void setMinGuests(int minGuests) {
        MinGuests = minGuests;
    }

    public int getMaxGuests() {
        return MaxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        MaxGuests = maxGuests;
    }

    public List<String> getAmenities() {
        return Amenities;
    }

    public void setAmenities(List<String> amenities) {
        Amenities = amenities;
    }

    public String getHostUsername() {
        return HostUsername;
    }

    public void setHostUsername(String hostUsername) {
        HostUsername = hostUsername;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Description);
        dest.writeString(Location);
        dest.writeString(Integer.toString(Price));
    }
}

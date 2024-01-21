package com.example.bookingapptim4.domain.models.accommodations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccommodationFilter implements Parcelable {
    String city;
    String checkin;
    String checkout;
    Integer guestNum;
    Double minPrice;
    Double maxPrice;
    List<String> amenities;
    List<AccommodationType> types;
    Double minRating = null;

    public AccommodationFilter() {
        amenities = new ArrayList<String>();
        types = new ArrayList<AccommodationType>();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public Integer getGuestNum() {
        return guestNum;
    }

    public void setGuestNum(Integer guestNum) {
        this.guestNum = guestNum;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<AccommodationType> getTypes() {
        return types;
    }

    public void setTypes(List<AccommodationType> types) {
        this.types = types;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    protected AccommodationFilter(Parcel in) {
        city = in.readString();
        checkin = in.readString();
        checkout = in.readString();
        if (in.readByte() == 0) {
            guestNum = null;
        } else {
            guestNum = in.readInt();
        }
        if (in.readByte() == 0) {
            minPrice = null;
        } else {
            minPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxPrice = null;
        } else {
            maxPrice = in.readDouble();
        }
        amenities = in.createStringArrayList();
        if (in.readByte() == 0) {
            minRating = null;
        } else {
            minRating = in.readDouble();
        }
    }

    public static final Creator<AccommodationFilter> CREATOR = new Creator<AccommodationFilter>() {
        @Override
        public AccommodationFilter createFromParcel(Parcel in) {
            return new AccommodationFilter(in);
        }

        @Override
        public AccommodationFilter[] newArray(int size) {
            return new AccommodationFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(checkin);
        dest.writeString(checkout);
        if (guestNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(guestNum);
        }
        if (minPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minPrice);
        }
        if (maxPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(maxPrice);
        }
        dest.writeStringList(amenities);
        if (minRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minRating);
        }
    }
}

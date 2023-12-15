package com.example.bookingapptim4.UI.Elements.Models.accommodation_handling;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AvailablePeriod implements Parcelable {
    private Long id;
    private TimeSlot timeSlot;
    private Float PricePerNight;

    protected AvailablePeriod(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            PricePerNight = null;
        } else {
            PricePerNight = in.readFloat();
        }
    }

    public static final Creator<AvailablePeriod> CREATOR = new Creator<AvailablePeriod>() {
        @Override
        public AvailablePeriod createFromParcel(Parcel in) {
            return new AvailablePeriod(in);
        }

        @Override
        public AvailablePeriod[] newArray(int size) {
            return new AvailablePeriod[size];
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
        if (PricePerNight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(PricePerNight);
        }
    }
}

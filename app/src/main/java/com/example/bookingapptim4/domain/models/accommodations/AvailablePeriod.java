package com.example.bookingapptim4.domain.models.accommodations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bookingapptim4.domain.models.shared.TimeSlot;

public class AvailablePeriod implements Parcelable {
    private Long id;
    private TimeSlot timeSlot;
    private Float pricePerNight;

    protected AvailablePeriod(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            pricePerNight = null;
        } else {
            pricePerNight = in.readFloat();
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
        if (pricePerNight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(pricePerNight);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Float getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Float pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}

package com.example.bookingapptim4.domain.models.shared;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TimeSlot implements Parcelable {
    private String startDate;
    private String endDate;

    public TimeSlot(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected TimeSlot(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

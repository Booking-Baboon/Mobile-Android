package com.example.bookingapptim4.domain.models.accommodations.summaries;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bookingapptim4.domain.models.shared.TimeSlot;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

public class MonthlySummary implements Parcelable {
    private Long accommodationId;
    private TimeSlot timeSlot;
    Map<Month, Integer> reservationsData;
    Map<Month, Double> profitData;
    public MonthlySummary() {
        reservationsData = new LinkedHashMap<>();
        profitData = new LinkedHashMap<>();
    }

    protected MonthlySummary(Parcel in) {
        if (in.readByte() == 0) {
            accommodationId = null;
        } else {
            accommodationId = in.readLong();
        }
        timeSlot = in.readParcelable(TimeSlot.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (accommodationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(accommodationId);
        }
        dest.writeParcelable(timeSlot, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MonthlySummary> CREATOR = new Creator<MonthlySummary>() {
        @Override
        public MonthlySummary createFromParcel(Parcel in) {
            return new MonthlySummary(in);
        }

        @Override
        public MonthlySummary[] newArray(int size) {
            return new MonthlySummary[size];
        }
    };

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Map<Month, Integer> getReservationsData() {
        return reservationsData;
    }

    public void setReservationsData(Map<Month, Integer> reservationsData) {
        this.reservationsData = reservationsData;
    }

    public Map<Month, Double> getProfitData() {
        return profitData;
    }

    public void setProfitData(Map<Month, Double> profitData) {
        this.profitData = profitData;
    }
}

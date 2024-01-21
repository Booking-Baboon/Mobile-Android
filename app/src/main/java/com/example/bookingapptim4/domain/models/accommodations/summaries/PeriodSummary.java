package com.example.bookingapptim4.domain.models.accommodations.summaries;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bookingapptim4.domain.models.shared.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class PeriodSummary implements Parcelable {
    private TimeSlot period;
    private List<AccommodationPeriodData> accommodationsData;
    public PeriodSummary() {
        accommodationsData = new ArrayList<>();
    }

    protected PeriodSummary(Parcel in) {
        period = in.readParcelable(TimeSlot.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(period, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PeriodSummary> CREATOR = new Creator<PeriodSummary>() {
        @Override
        public PeriodSummary createFromParcel(Parcel in) {
            return new PeriodSummary(in);
        }

        @Override
        public PeriodSummary[] newArray(int size) {
            return new PeriodSummary[size];
        }
    };

    public TimeSlot getPeriod() {
        return period;
    }

    public void setPeriod(TimeSlot period) {
        this.period = period;
    }

    public List<AccommodationPeriodData> getAccommodationsData() {
        return accommodationsData;
    }

    public void setAccommodationsData(List<AccommodationPeriodData> accommodationsData) {
        this.accommodationsData = accommodationsData;
    }
}

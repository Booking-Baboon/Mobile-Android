package com.example.bookingapptim4.domain.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bookingapptim4.domain.models.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

public class Host extends User implements Parcelable {
    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();

    public static final Creator<Host> CREATOR = new Creator<Host>() {
        @Override
        public Host createFromParcel(Parcel in) {
            return new Host(in);
        }

        @Override
        public Host[] newArray(int size) {
            return new Host[size];
        }
    };

    protected Host(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}

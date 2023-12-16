package com.example.bookingapptim4.domain.models.users;

import android.os.Parcel;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

public class Guest extends User {

    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();;

    private Set<Accommodation> favorites = new HashSet<Accommodation>();

    protected Guest(Parcel in) {
        super(in);
    }
}
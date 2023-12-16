package com.example.bookingapptim4.domain.models.users;

import com.example.bookingapptim4.domain.models.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

public class Host extends User {
    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();
}

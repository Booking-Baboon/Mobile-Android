package com.example.bookingapptim4.domain.models.notifications;

import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class Notification {
    private Long id;

    private String message;

    private NotificationType type;

    private Date timeCreated;

    private Boolean isRead = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification(Long id, String message, NotificationType type, Date timeCreated, Boolean isRead, User user) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.timeCreated = timeCreated;
        this.isRead = isRead;
        this.user = user;
    }

    public Notification(String message, NotificationType type, Date timeCreated, User user) {
        this.message = message;
        this.type = type;
        this.timeCreated = timeCreated;
        this.user = user;
    }

    public Notification() {}

    private User user;
}

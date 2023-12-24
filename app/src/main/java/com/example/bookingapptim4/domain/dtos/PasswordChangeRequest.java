package com.example.bookingapptim4.domain.dtos;

public class PasswordChangeRequest{
    private String currentPassword;
    private String newPassword;

    public PasswordChangeRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public PasswordChangeRequest() {}
}


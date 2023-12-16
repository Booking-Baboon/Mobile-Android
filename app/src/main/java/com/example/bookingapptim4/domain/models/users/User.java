package com.example.bookingapptim4.domain.models.users;


public class User{

    private Long id;

    private String password;

    private UserStatus status = UserStatus.Inactive;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private Role role = Role.GUEST;

    private String jwt;

}

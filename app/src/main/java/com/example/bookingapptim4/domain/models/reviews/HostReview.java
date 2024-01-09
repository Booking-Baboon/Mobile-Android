package com.example.bookingapptim4.domain.models.reviews;

import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class HostReview extends Review{
    private Host reviewedHost;


    public HostReview(User reviewer, Date createdOn, short rating, String comment, Host reviewedHost) {
        super(reviewer, createdOn, rating, comment);
        this.reviewedHost = reviewedHost;
    }

    public Host getReviewedHost() {
        return reviewedHost;
    }
}

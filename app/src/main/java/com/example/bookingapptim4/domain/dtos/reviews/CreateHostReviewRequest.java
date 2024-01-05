package com.example.bookingapptim4.domain.dtos.reviews;

import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class CreateHostReviewRequest {
    protected UserReference reviewer;
    protected String createdOn;
    protected short rating;
    protected String comment;

    protected UserReference reviewedHost;

    public CreateHostReviewRequest(UserReference reviewer, String createdOn, short rating, String comment, UserReference reviewedHost) {
        this.reviewer = reviewer;
        this.createdOn = createdOn;
        this.rating = rating;
        this.comment = comment;
        this.reviewedHost = reviewedHost;
    }
}

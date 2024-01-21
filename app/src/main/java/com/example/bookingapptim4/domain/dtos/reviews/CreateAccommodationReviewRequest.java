package com.example.bookingapptim4.domain.dtos.reviews;

import com.example.bookingapptim4.domain.dtos.accommodations.AccommodationReference;
import com.example.bookingapptim4.domain.dtos.users.UserReference;

public class CreateAccommodationReviewRequest {
    protected UserReference reviewer;
    protected String createdOn;
    protected short rating;
    protected String comment;

    protected AccommodationReference reviewedAccommodation;

    public CreateAccommodationReviewRequest(UserReference reviewer, String createdOn, short rating, String comment, AccommodationReference reviewedAccommodation) {
        this.reviewer = reviewer;
        this.createdOn = createdOn;
        this.rating = rating;
        this.comment = comment;
        this.reviewedAccommodation = reviewedAccommodation;
    }
}

package com.example.bookingapptim4.domain.models.reviews;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.users.User;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AccommodationReview extends Review{

    private Accommodation reviewedAccommodation;

    public Accommodation getReviewedAccommodation() {
        return reviewedAccommodation;
    }

    @Override
    public String getReviewedName(){
        return reviewedAccommodation.getName();
    }

    public AccommodationReview(Accommodation reviewedAccommodation) {
        this.reviewedAccommodation = reviewedAccommodation;
    }

    public AccommodationReview(User reviewer, Date createdOn, short rating, String comment, Accommodation reviewedAccommodation) {
        super(reviewer, createdOn, rating, comment);
        this.reviewedAccommodation = reviewedAccommodation;
    }

    public void setReviewedAccommodation(Accommodation reviewedAccommodation) {
        this.reviewedAccommodation = reviewedAccommodation;
    }
}

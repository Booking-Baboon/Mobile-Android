package com.example.bookingapptim4.domain.models.reviews;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class AccommodationReview {
    private Long id;
    private User reviewer;
    private String createdOn;
    private short rating;
    private String comment;
    private Accommodation reviewedAccommodation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Accommodation getReviewedAccommodation() {
        return reviewedAccommodation;
    }

    public void setReviewedAccommodation(Accommodation reviewedAccommodation) {
        this.reviewedAccommodation = reviewedAccommodation;
    }
}

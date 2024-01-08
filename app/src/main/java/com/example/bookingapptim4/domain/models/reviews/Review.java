package com.example.bookingapptim4.domain.models.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class Review implements Parcelable {
    protected Long id;
    protected User reviewer;
    protected Date createdOn;
    protected short rating;
    protected String comment;

    public Review() {
    }

    public Review(User reviewer, Date createdOn, short rating, String comment) {
        this.reviewer = reviewer;
        this.createdOn = createdOn;
        this.rating = rating;
        this.comment = comment;
    }

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

    public Date getCreatedOnDate() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
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

    public String getCreatedOn() {
        return createdOn.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(reviewer, flags);
        dest.writeString(createdOn.toString());
        dest.writeInt(rating);
        dest.writeString(comment);

    }
}

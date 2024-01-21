package com.example.bookingapptim4.domain.models.reports;

import com.example.bookingapptim4.domain.models.reviews.Review;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class ReviewReport extends Report{

    private Review reportedReview;

    public ReviewReport(User reportee, Date createdOn, ReportStatus status, String message, Review reportedReview) {
        super(reportee, createdOn, status, message);
        this.reportedReview = reportedReview;
    }

    public ReviewReport(User reportee, Date createdOn, ReportStatus status, String message) {
        super(reportee, createdOn, status, message);
    }

    public Review getReportedReview() {
        return reportedReview;
    }

    public void setReportedReview(Review reportedReview) {
        this.reportedReview = reportedReview;
    }
}

package com.example.bookingapptim4.domain.dtos.reports;

import com.example.bookingapptim4.domain.dtos.reviews.ReviewReference;
import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reports.ReportStatus;

public class CreateReviewReportRequest {
    private UserReference reportee;
    private String createdOn;
    private ReportStatus status;
    private String message;

    public CreateReviewReportRequest(UserReference reportee, String createdOn, ReportStatus status, String message, ReviewReference reportedReview) {
        this.reportee = reportee;
        this.createdOn = createdOn;
        this.status = status;
        this.message = message;
        this.reportedReview = reportedReview;
    }

    private ReviewReference reportedReview;
}

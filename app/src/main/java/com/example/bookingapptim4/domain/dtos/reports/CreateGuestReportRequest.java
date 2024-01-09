package com.example.bookingapptim4.domain.dtos.reports;

import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reports.ReportStatus;

public class CreateGuestReportRequest {
    private UserReference reportee;
    private String createdOn;
    private ReportStatus status;
    private String message;

    public CreateGuestReportRequest(UserReference reportee, String createdOn, ReportStatus status, String message, UserReference reportedGuest) {
        this.reportee = reportee;
        this.createdOn = createdOn;
        this.status = status;
        this.message = message;
        this.reportedGuest = reportedGuest;
    }

    private UserReference reportedGuest;
}

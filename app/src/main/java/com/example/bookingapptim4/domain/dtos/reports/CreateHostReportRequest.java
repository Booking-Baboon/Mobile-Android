package com.example.bookingapptim4.domain.dtos.reports;

import com.example.bookingapptim4.domain.dtos.users.UserReference;
import com.example.bookingapptim4.domain.models.reports.ReportStatus;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class CreateHostReportRequest {
    private UserReference reportee;
    private String createdOn;
    private ReportStatus status;
    private String message;

    public CreateHostReportRequest(UserReference reportee, String createdOn, ReportStatus status, String message, UserReference reportedHost) {
        this.reportee = reportee;
        this.createdOn = createdOn;
        this.status = status;
        this.message = message;
        this.reportedHost = reportedHost;
    }

    private UserReference reportedHost;
}

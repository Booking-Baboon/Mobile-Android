package com.example.bookingapptim4.domain.models.reports;

import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class GuestReport extends Report {

    private Guest reportedGuest;

    public GuestReport(User reportee, Date createdOn, ReportStatus status, String message, Guest reportedGuest) {
        super(reportee, createdOn, status, message);
        this.reportedGuest = reportedGuest;
    }

    public GuestReport(User reportee, Date createdOn, ReportStatus status, String message) {
        super(reportee, createdOn, status, message);
    }
}

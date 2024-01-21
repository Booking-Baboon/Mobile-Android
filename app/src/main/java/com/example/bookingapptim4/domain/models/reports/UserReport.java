package com.example.bookingapptim4.domain.models.reports;

import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class UserReport extends Report{

    private Guest reportedGuest;
    private Host reportedHost;

    public UserReport(User reportee, Date createdOn, ReportStatus status, String message) {
        super(reportee, createdOn, status, message);
    }

    public UserReport(User reportee, Date createdOn, ReportStatus status, String message, Guest reportedGuest, Host reportedHost) {
        super(reportee, createdOn, status, message);
        this.reportedGuest = reportedGuest;
        this.reportedHost = reportedHost;
    }

    public Guest getReportedGuest() {
        return reportedGuest;
    }

    public Host getReportedHost() {
        return reportedHost;
    }
}

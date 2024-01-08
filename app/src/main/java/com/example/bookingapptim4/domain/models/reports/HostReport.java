package com.example.bookingapptim4.domain.models.reports;

import com.example.bookingapptim4.domain.models.users.Host;
import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class HostReport extends Report{

    private Host reportedHost;

    public HostReport(User reportee, Date createdOn, ReportStatus status, String message, Host reportedHost) {
        super(reportee, createdOn, status, message);
        this.reportedHost = reportedHost;
    }

    public HostReport(User reportee, Date createdOn, ReportStatus status, String message) {
        super(reportee, createdOn, status, message);
    }
}

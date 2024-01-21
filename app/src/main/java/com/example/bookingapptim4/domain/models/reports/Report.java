package com.example.bookingapptim4.domain.models.reports;

import com.example.bookingapptim4.domain.models.users.User;

import java.util.Date;

public class Report {
    private Long id;

    private User reportee;

    private Date createdOn;

    private ReportStatus status = ReportStatus.Pending;
    private String message;

    public Report(User reportee, Date createdOn, ReportStatus status, String message) {
        this.reportee = reportee;
        this.createdOn = createdOn;
        this.status = status;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReportee() {
        return reportee;
    }

    public void setReportee(User reportee) {
        this.reportee = reportee;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

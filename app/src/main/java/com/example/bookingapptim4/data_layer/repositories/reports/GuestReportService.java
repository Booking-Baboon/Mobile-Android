package com.example.bookingapptim4.data_layer.repositories.reports;

import com.example.bookingapptim4.domain.dtos.reports.CreateGuestReportRequest;
import com.example.bookingapptim4.domain.dtos.reports.CreateHostReportRequest;
import com.example.bookingapptim4.domain.models.reports.GuestReport;
import com.example.bookingapptim4.domain.models.reports.HostReport;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GuestReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("guest-reports/")
    Call<GuestReport> create(@Body CreateGuestReportRequest report, @Header("Authorization") String authorizationHeader);
}

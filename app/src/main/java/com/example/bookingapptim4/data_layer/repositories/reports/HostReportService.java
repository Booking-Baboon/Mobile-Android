package com.example.bookingapptim4.data_layer.repositories.reports;

import com.example.bookingapptim4.domain.dtos.reports.CreateHostReportRequest;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.models.reports.HostReport;
import com.example.bookingapptim4.domain.models.reviews.HostReview;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HostReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("host-reports/")
    Call<HostReport> create(@Body CreateHostReportRequest review, @Header("Authorization") String authorizationHeader);

}

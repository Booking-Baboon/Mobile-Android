package com.example.bookingapptim4.data_layer.repositories.reports;

import com.example.bookingapptim4.domain.dtos.reports.CreateHostReportRequest;
import com.example.bookingapptim4.domain.dtos.reports.CreateReviewReportRequest;
import com.example.bookingapptim4.domain.models.reports.HostReport;
import com.example.bookingapptim4.domain.models.reports.ReviewReport;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReviewReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("review-reports/")
    Call<ReviewReport> create(@Body CreateReviewReportRequest review, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("review-reports")
    Call<ArrayList<ReviewReport>> getAll(@Header("Authorization") String authorizationHeader);

}

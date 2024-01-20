package com.example.bookingapptim4.data_layer.repositories.reports;

import com.example.bookingapptim4.domain.dtos.reports.CreateHostReportRequest;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.models.reports.HostReport;
import com.example.bookingapptim4.domain.models.reports.ReviewReport;
import com.example.bookingapptim4.domain.models.reports.UserReport;
import com.example.bookingapptim4.domain.models.reviews.HostReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("user-reports")
    Call<ArrayList<UserReport>> getAll(@Header("Authorization") String authorizationHeader);

}

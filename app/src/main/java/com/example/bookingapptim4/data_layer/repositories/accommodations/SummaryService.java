package com.example.bookingapptim4.data_layer.repositories.accommodations;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.domain.models.accommodations.summaries.MonthlySummary;
import com.example.bookingapptim4.domain.models.accommodations.summaries.PeriodSummary;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SummaryService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("summary/monthly/{accommodationId}")
    Call<MonthlySummary> getMonthlySummary(@Path("accommodationId") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("summary/monthly/{accommodationId}/pdf")
    Call<ResponseBody> getMonthlySummaryPDF(@Path("accommodationId") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("summary/period")
    Call<PeriodSummary> getPeriodSummary(
            @Query("host-id") Long hostId,
            @Query("start-date") String startDate,
            @Query("end-date") String endDate,
            @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("summary/period/pdf")
    Call<ResponseBody> getPeriodSummaryPDF(
            @Query("host-id") Long hostId,
            @Query("start-date") String startDate,
            @Query("end-date") String endDate,
            @Header("Authorization") String authorizationHeader);

}

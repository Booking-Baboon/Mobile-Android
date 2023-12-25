package com.example.bookingapptim4.data_layer.repositories.accommodations;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationRequest;
import com.example.bookingapptim4.domain.models.accommodations.AvailablePeriod;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AvailablePeriodService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("available-periods")
    Call<AvailablePeriod> create(@Body AvailablePeriod period);
}

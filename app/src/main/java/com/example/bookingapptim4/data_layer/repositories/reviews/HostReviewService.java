package com.example.bookingapptim4.data_layer.repositories.reviews;

import com.example.bookingapptim4.domain.dtos.reservations.CreateReservationRequest;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.models.reservations.Reservation;
import com.example.bookingapptim4.domain.models.reviews.HostReview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HostReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("host-reviews/")
    Call<HostReview> create(@Body CreateHostReviewRequest review, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("host-reviews/host/{hostId}")
    Call<List<HostReview>> getAllByHost(@Path("hostId") Long hostId, @Header("Authorization") String authorizationHeader);

}

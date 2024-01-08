package com.example.bookingapptim4.data_layer.repositories.reviews;

import com.example.bookingapptim4.domain.dtos.reviews.CreateAccommodationReviewRequest;
import com.example.bookingapptim4.domain.dtos.reviews.CreateHostReviewRequest;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;
import com.example.bookingapptim4.domain.models.reviews.HostReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodation-reviews/accommodation/{accommodationId}")
    Call<ArrayList<AccommodationReview>> getAccommodationReviews(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodation-reviews/average-rating/{accommodationId}")
    Call<Float> getAverateRating(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodation-reviews/")
    Call<AccommodationReview> create(@Body CreateAccommodationReviewRequest review, @Header("Authorization") String authorizationHeader);

}

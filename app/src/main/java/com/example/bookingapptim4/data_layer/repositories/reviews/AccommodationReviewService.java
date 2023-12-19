package com.example.bookingapptim4.data_layer.repositories.reviews;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.reviews.AccommodationReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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


}

package com.example.bookingapptim4.data_layer.repositories.accommodations;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations")
    Call<ArrayList<Accommodation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{id}")
    Call<Accommodation> getById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations")
    Call<Accommodation> create(@Body Accommodation accommodation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/{id}")
    Call<ResponseBody> remove(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations")
    Call<Accommodation> edit(@Body Accommodation accommodation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/filter")
    Call<ArrayList<Accommodation>> search(
            @Query("city") String city,
            @Query("checkin") String checkin,
            @Query("checkout") String checkout,
            @Query("guest-num") Integer guestNum,
            @Query("min-price") Double minPrice,
            @Query("max-price") Double maxPrice,
            @Query("amenities") String amenities,
            @Query("property-type") String propertyType,
            @Query("min-rating") Double minRating
    );


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{id}/total-price")
    Call<Float> getTotalPrice(
            @Path("id") Long id,
            @Query("checkin") String checkin,
            @Query("checkout") String checkout
    );
}

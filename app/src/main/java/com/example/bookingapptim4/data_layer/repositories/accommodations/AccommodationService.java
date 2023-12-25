package com.example.bookingapptim4.data_layer.repositories.accommodations;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationRequest;

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
    Call<Accommodation> create(@Body AccommodationRequest accommodation, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/{id}")
    Call<Accommodation> remove(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/host/{id}")
    Call<ArrayList<Accommodation>> getAllByHost(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations/{accommodationId}/updateEditingStatus/{isBeingEdited}")
    Call<Accommodation> updateEditingStatus(@Path("accommodationId") Long accommodationId, @Path("isBeingEdited") boolean isBeingEdited, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations/{accommodationId}/addPeriod/{periodId}")
    Call<Accommodation> addPeriod(@Path("accommodationId") Long accommodationId,@Path("periodId") Long periodId, @Header("Authorization") String authorizationHeader );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations//{accommodationId}/add/{imageId}")
    Call<Accommodation> addImage(@Path("accommodationId") Long accommodationId,@Path("imageId") Long periodId );


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/{accommodationId}/available-periods/{periodId}")
    Call<Accommodation> remove(@Path("accommodationId") Long accommodationId,@Path("periodId") Long periodId);

}

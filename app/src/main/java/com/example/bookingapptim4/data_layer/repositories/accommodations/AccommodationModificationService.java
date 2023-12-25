package com.example.bookingapptim4.data_layer.repositories.accommodations;

import com.example.bookingapptim4.domain.dtos.accommodations.AccommodationModificationCreateRequest;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModification;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccommodationModificationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodation-modifications")
    Call<ArrayList<AccommodationModification>> getAll(@Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodation-modifications/approve/{id}")
    Call<AccommodationModification> approve(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodation-modifications/deny/{id}")
    Call<AccommodationModification> deny(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodation-modifications")
    Call<AccommodationModification> create(@Body AccommodationModificationCreateRequest accommodationModification, @Header("Authorization") String authorizationHeader);
}

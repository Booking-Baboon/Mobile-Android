package com.example.bookingapptim4.data_layer.repositories.reservations;

import com.example.bookingapptim4.domain.dtos.reservations.CreateReservationRequest;
import com.example.bookingapptim4.domain.models.reservations.Reservation;

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

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations")
    Call<ArrayList<Reservation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/{id}")
    Call<Reservation> getById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations")
    Call<Reservation> create(@Body CreateReservationRequest reservation, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("reservations/{id}")
    Call<ResponseBody> remove(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reservations")
    Call<Reservation> edit(@Body Reservation reservation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/guest/{id}")
    Call<ArrayList<Reservation>> getAllForGuest(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/host/{id}")
    Call<ArrayList<Reservation>> getAllForHost(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/cancellation-count/{id}")
    Call<Long> getGuestCancellationCount(@Path("id") Long id, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reservations/{id}/cancel")
    Call<Reservation> cancel(@Path("id") Long id, @Header("Authorization") String authorizationHeader);


}

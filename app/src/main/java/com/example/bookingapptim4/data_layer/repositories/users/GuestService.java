package com.example.bookingapptim4.data_layer.repositories.users;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.models.users.UserUpdateRequest;

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

public interface GuestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("guests/")
    Call<Guest> create(@Body Guest guest);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("guests/")
    Call<Guest> edit(@Body UserUpdateRequest guest, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("guests/{guestId}")
    Call<Guest> remove(@Path("guestId") Long guestId, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("guests/{guestId}/favorite-accommodations")
    Call<ArrayList<Accommodation>> getFavorites(@Path("guestId") Long guestId, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("guests/{guestId}/favorite-accommodations/add/{accommodationId}")
    Call<Guest> addFavorite(@Path("guestId") Long guestId, @Path("accommodationId") Long accommodationId, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("guests/{guestId}/favorite-accommodations/remove/{accommodationId}")
    Call<Guest> removeFavorite(@Path("guestId") Long guestId, @Path("accommodationId") Long accommodationId, @Header("Authorization") String authorizationHeader);


}

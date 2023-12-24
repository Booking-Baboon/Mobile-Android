package com.example.bookingapptim4.data_layer.repositories.users;

import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.domain.models.users.UserUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
}

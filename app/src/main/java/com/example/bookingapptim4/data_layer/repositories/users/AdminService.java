package com.example.bookingapptim4.data_layer.repositories.users;

import com.example.bookingapptim4.domain.models.users.Admin;
import com.example.bookingapptim4.domain.models.users.Guest;
import com.example.bookingapptim4.domain.models.users.UserUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdminService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("admin/")
    Call<Admin> create(@Body Admin admin);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("admin/")
    Call<Admin> edit(@Body UserUpdateRequest admin, @Header("Authorization") String authorizationHeader);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("admins/{adminId}")
    Call<Admin> remove(@Path("adminId") Long adminId, @Header("Authorization") String authorizationHeader);
}

package com.example.bookingapptim4.data_layer.repositories.users;

import com.example.bookingapptim4.domain.models.users.Host;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HostService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("hosts/")
    Call<Host> create(@Body Host host);
}

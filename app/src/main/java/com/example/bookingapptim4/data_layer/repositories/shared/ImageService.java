package com.example.bookingapptim4.data_layer.repositories.shared;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.shared.Image;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("images/{id}")
    Call<ResponseBody> getById(@Path("id") Long id);

    @Multipart
    @Headers({
            "User-Agent: Mobile-Android",
    })
    @POST("images")

    Call<Image> create(
            @Part("path") RequestBody path,
            @Part("fileName") RequestBody fileName,
            @Part MultipartBody.Part content
    );

}

package com.example.bookingapptim4.data_layer.repositories.shared;

import android.util.Base64;

import com.example.bookingapptim4.BuildConfig;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationService;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageUtils {
    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":8080/api/v1/";

    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .client(test())
            .build();

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public static ImageService imageService = retrofit.create(ImageService.class);

}

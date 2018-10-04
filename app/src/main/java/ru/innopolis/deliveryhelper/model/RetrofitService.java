package ru.innopolis.deliveryhelper.model;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static final String BASE_URL = "http://127.0.0.1:8333/";

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();
        }
        return retrofit;
    }
}
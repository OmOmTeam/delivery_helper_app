package ru.innopolis.deliveryhelper.model;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

//    private static final String BASE_URL = "http://10.240.20.162:5321/";

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getIP())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();
        }
        return retrofit;
    }

    private static String getIP(){
        return String.format("http://%s:5321/", SafeStorage.getAddress());
    }
}

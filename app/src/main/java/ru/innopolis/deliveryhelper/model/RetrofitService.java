package ru.innopolis.deliveryhelper.model;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.model.PlainConsts;

import static ru.innopolis.deliveryhelper.model.PlainConsts.BASE_URL;

public class RetrofitService {


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

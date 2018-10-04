package ru.innopolis.deliveryhelper.model;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("available_orders")
    Call<ItemResponseModel> getOrders(@Header("token") String token );

    @POST("login")
    Call<LoginResponseModel> login(@Body RequestBody params, @Field("login") String login, @Field("password") String password);
}

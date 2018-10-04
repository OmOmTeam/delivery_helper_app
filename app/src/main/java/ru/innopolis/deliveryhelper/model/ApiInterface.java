package ru.innopolis.deliveryhelper.model;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiInterface {

    Call<LoginModel> login(@Body RequestBody params);
}

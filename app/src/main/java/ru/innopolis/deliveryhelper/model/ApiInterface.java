package ru.innopolis.deliveryhelper.model;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.innopolis.deliveryhelper.model.dataframes.response.AcceptanceModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemResponseModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.LoginResponseModel;

public interface ApiInterface {

    @POST("login")
    Call<LoginResponseModel> login(@Body RequestBody credentials);

    @POST("available_orders")
    Call<List<ItemHeaderResponseModel>> getOrderList(@Header("Token") String token);

    @POST("assigned_orders")
    Call<List<ItemHeaderResponseModel>> getAssignedOrderList(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/details")
    Call<ItemResponseModel> getOrderDetails(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/accept")
    Call<AcceptanceModel> requestAssignOrder(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/pick")
    Call<AcceptanceModel> requestPickOrder(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/validate_customer")
    Call<AcceptanceModel> requestValidateCustomer(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/deliver")
    Call<AcceptanceModel> requestDeliverOrder(@Header("Token") String token, @Body RequestBody parameters);

    @POST("order/cancel")
    Call<AcceptanceModel> requestDismissOrder(@Header("Token") String token, @Body RequestBody parameters);

    @POST("location/update")
    Call<AcceptanceModel> requestUpdateLocation(@Header("Token") String token, @Body RequestBody parameters);

}

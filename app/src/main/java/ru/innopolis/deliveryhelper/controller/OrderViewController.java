package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.GetDetailsRequestModel;
import ru.innopolis.deliveryhelper.model.ItemHeaderResponseModel;
import ru.innopolis.deliveryhelper.model.ItemResponseModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;

public class OrderViewController implements OrderViewMVC.Controller {


    private final String TAG = "LoginController";
    private OrderViewMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public OrderViewController(OrderViewMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    public void loadDetailList(String orderId) {
        try {
            GetDetailsRequestModel gtrm = new GetDetailsRequestModel(orderId);
            Call<ItemResponseModel> call = api.getOrderDetails(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<ItemResponseModel>() {
                @Override
                public void onResponse(Call<ItemResponseModel> call, Response<ItemResponseModel> response) {
                    if (response.body() != null) {

                        ItemResponseModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            view.addDetailEntity("Order ID", irm.getItemId());
                            view.addDetailEntity("Destination", irm.getDestinationAddress());
                            view.addDetailEntity("Customer", irm.getCustomerName());
                            view.addDetailEntity("Customer Phone", irm.getCustomerPhone());
                            view.hideProgressBar();
                        }else{
                            view.showNotification(irm.getError());
                        }
                    } else {
                        view.showNotification("Server error");
                    }
                }

                @Override
                public void onFailure(Call<ItemResponseModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification("Server Connection Error");
                    call.cancel();
                }
            });
        } catch (Exception e) {
            view.showNotification(e.getMessage());
        }
    }
}

package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;

public class OrderListController implements OrderListMVC.Controller {


    private final String TAG = "OrderListController";
    private OrderListMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public OrderListController(OrderListMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    public void loadOrderList() {
        try {
            view.showProgressBar();
            view.clearList();
            Call<List<ItemHeaderResponseModel>> call = api.getOrderList(SafeStorage.getToken());
            call.enqueue(new Callback<List<ItemHeaderResponseModel>>() {
                @Override
                public void onResponse(Call<List<ItemHeaderResponseModel>> call, Response<List<ItemHeaderResponseModel>> response) {
                    if (response.body() != null) {
                        view.updateList(response.body());
                        view.hideProgressBar();
                    } else {
                        view.showNotification("Server error");
                    }
                }

                @Override
                public void onFailure(Call<List<ItemHeaderResponseModel>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification("Server Connection Error");
                    call.cancel();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}

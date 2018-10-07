package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.ItemHeaderResponseModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;

public class OrderListController implements OrderListMVC.Controller {


    private final String TAG = "LoginController";
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
            Call<List<ItemHeaderResponseModel>> call = api.getOrderList(SafeStorage.getToken());
            call.enqueue(new Callback<List<ItemHeaderResponseModel>>() {
                @Override
                public void onResponse(Call<List<ItemHeaderResponseModel>> call, Response<List<ItemHeaderResponseModel>> response) {
                    if (response.body() != null) {
                        List<ItemHeaderResponseModel> ihrm = response.body();
                        for(ItemHeaderResponseModel m: ihrm) {
                            view.addEntity(m.getItemId(),m.getTitle(), m.getAddress(), m.getWeight(), m.getDimensions(), m.getDistanceFromWarehouse(), m.getItemType());
                        }
                        view.updateList();
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
            view.showNotification(e.getMessage());
        }
    }
}

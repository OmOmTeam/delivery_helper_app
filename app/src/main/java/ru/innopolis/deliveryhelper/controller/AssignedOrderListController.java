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
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.model.dataframes.request.ItemRequestModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

public class AssignedOrderListController implements OrderListMVC.Controller {


    private final String TAG = "LoginController";
    private OrderListMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public AssignedOrderListController(OrderListMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    public void loadOrderList() {
        try {
            view.showProgressBar();
            view.clearList();
            ItemRequestModel irm = new ItemRequestModel("", SafeStorage.getUsername());
            Call<List<ItemHeaderResponseModel>> call = api.getAssignedOrderList(SafeStorage.getToken(),RequestBody.create(MediaType.parse("application/json"), gson.toJson(irm)));
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

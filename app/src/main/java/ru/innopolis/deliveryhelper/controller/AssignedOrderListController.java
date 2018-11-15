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
import ru.innopolis.deliveryhelper.model.dataframes.request.LoginOnlyModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.internal_server_error_message;
import static ru.innopolis.deliveryhelper.model.PlainConsts.server_connection_error_message;

public class AssignedOrderListController implements OrderListMVC.Controller {


    private final String TAG = "AssignedOrderListController";
    private OrderListMVC.View view;
    private ApiInterface api;
    private Gson gson;

    /**
     * Constructor
     *
     * @param view Calling view
     */
    public AssignedOrderListController(OrderListMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    /**
     * Load list of assigned orders from server and if successful, update view, otherwise show error message received from server.
     */
    public void loadOrderList() {
        try {
            // show loading animation while not finished
            view.showProgressBar();
            view.clearList();

            // create request dataframe
            LoginOnlyModel lom = new LoginOnlyModel(SafeStorage.getUsername());
            // convert dataframe to json and perform call to server
            Call<List<ItemHeaderResponseModel>> call = api.getAssignedOrderList(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(lom)));
            call.enqueue(new Callback<List<ItemHeaderResponseModel>>() {
                @Override
                public void onResponse(Call<List<ItemHeaderResponseModel>> call, Response<List<ItemHeaderResponseModel>> response) {
                    if (response.body() != null) {
                        view.updateList(response.body());
                        view.hideProgressBar();
                    } else {
                        view.showNotification(internal_server_error_message);
                    }
                }

                @Override
                public void onFailure(Call<List<ItemHeaderResponseModel>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification(server_connection_error_message);
                    call.cancel();
                }
            });
        } catch (Exception e) {
            view.showNotification(e.getMessage());
        }

        // finish loading animation
        view.hideRefreshing();
    }
}

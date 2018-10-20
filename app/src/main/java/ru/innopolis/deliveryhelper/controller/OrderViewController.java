package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.dataframes.request.ItemRequestModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.AcceptanceModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemResponseModel;
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

    public void requestAssignOrder(final String orderId){
        try {
            ItemRequestModel gtrm = new ItemRequestModel(orderId,SafeStorage.getUsername());
            Call<AcceptanceModel> call = api.requestAssignOrder(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            loadDetailList(orderId);
                        }else{
                            view.showNotification(irm.getError());
                        }
                    } else {
                        view.showNotification("Server error");
                    }
                }

                @Override
                public void onFailure(Call<AcceptanceModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification("Server Connection Error");
                    call.cancel();
                }
            });
        } catch (Exception e) {
            view.showNotification(e.getMessage());
        }
    }

    public void loadDetailList(String orderId) {
        try {
            ItemRequestModel gtrm = new ItemRequestModel(orderId,SafeStorage.getUsername());
//            view.showNotification("Sent request with: "+orderId+" "+SafeStorage.getUsername());
            Call<ItemResponseModel> call = api.getOrderDetails(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<ItemResponseModel>() {
                @Override
                public void onResponse(Call<ItemResponseModel> call, Response<ItemResponseModel> response) {
                    if (response.body() != null) {

                        ItemResponseModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            view.addDetailEntity("Order ID", irm.getItemId());
                            view.addDetailEntity("Location", irm.getDestination());
                            view.addDetailEntity("Address", irm.getDestinationAddress());
                            view.addDetailEntity("Dimensions", irm.getDimensions());
                            view.addDetailEntity("Weight", irm.getWeight());
                            view.addDetailEntity("Status", irm.getDeliveryState());
                            view.addDetailEntity("Customer", irm.getCustomerName());
                            view.addDetailEntity("Customer Phone", irm.getCustomerPhone());
                            view.addDetailEntity("Assigned To",irm.getAssignedTo());
                            view.addDetailEntity("Delivery Time From",irm.getDeliveryTimeFrom());
                            view.addDetailEntity("Delivery Time To",irm.getDeliveryTimeTo());
                            view.hideProgressBar();
                            view.setActionState(1);
                            view.setAssignedPanelState(1);
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

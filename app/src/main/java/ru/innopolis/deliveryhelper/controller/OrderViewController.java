package ru.innopolis.deliveryhelper.controller;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.model.dataframes.request.ItemRequestModel;
import ru.innopolis.deliveryhelper.model.dataframes.request.ItemRequestWKeyModel;
import ru.innopolis.deliveryhelper.model.dataframes.request.LoginOnlyModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.AcceptanceModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemResponseModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.NumberResponseModel;

public class OrderViewController implements OrderViewMVC.Controller {


    private final String TAG = "LoginController";
    private OrderViewMVC.View view;
    private ApiInterface api;
    private Gson gson;

    /**
     * Constructor of controller
     * @param view calling view
     */
    public OrderViewController(OrderViewMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    /**
     * Request details list for the order and build UI properties based on it
     * @param orderId id of given order
     */
    public void loadDetailList(String orderId) {
        try {
            view.resetLoad();
            String username = SafeStorage.getUsername();
            ItemRequestModel gtrm = new ItemRequestModel(orderId, username);
            Log.d(TAG, String.format("loadDetailList: orderId - %s, username - %s", orderId, username));
            Call<ItemResponseModel> call = api.getOrderDetails(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<ItemResponseModel>() {
                @Override
                public void onResponse(Call<ItemResponseModel> call, Response<ItemResponseModel> response) {
                    if (response.body() != null) {

                        ItemResponseModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            view.loadMap(irm.getWarehouseLocation(), irm.getRecipientLocation());

                            for(Pair<String, String> pair : irm.getAvailableParameters()) {
                                view.addDetailEntity(pair.first, pair.second);
                            }
                            view.showProgressBar(false);
                            view.setActionState(Integer.parseInt(irm.getStateCode()));
                            view.setCustomer(irm.getRecipientName(), irm.getRecipientPhone());
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

    /**
     * Request the order to be assigned to given account
     * @param orderId id of given order
     */
    @Override
    public void acceptOrder(String orderId) {
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

    /**
     * Request the order to be marked as picked
     * @param orderId id of given order
     * @param key code for parcel pick approval
     */
    @Override
    public void pickOrder(String orderId, String key) {
        try {
            ItemRequestWKeyModel gtrm = new ItemRequestWKeyModel(orderId, SafeStorage.getUsername(), key);
            Call<AcceptanceModel> call = api.requestPickOrder(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            loadDetailList(orderId);
                            view.showNotification("Order picked successfully");
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

    /**
     * Request sending of SMS message to customer with code that is needed for approval of order delivery
     * @param orderId id of given order
     */
    @Override
    public void validateRecipient(String orderId) {
        try {
            ItemRequestModel gtrm = new ItemRequestModel(orderId, SafeStorage.getUsername());
            Call<AcceptanceModel> call = api.requestValidateCustomer(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            loadDetailList(orderId);
                            view.showNotification("New SMS was sent to recipient");
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

    /**
     * Request the order delivery to me approved, with a validation code
     * @param orderId id of given order
     * @param key code that was received by customer
     */
    @Override
    public void deliverOrder(String orderId, String key) {
        try {
            ItemRequestWKeyModel gtrm = new ItemRequestWKeyModel(orderId, SafeStorage.getUsername(), key);
            Call<AcceptanceModel> call = api.requestDeliverOrder(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            loadDetailList(orderId);
                            view.showNotification("Order is now marked as delivered");
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

    /**
     * Request the order to be cancelled and made available for other delivery operators again
     * @param orderId id of given order
     */
    @Override
    public void cancelOrder(String orderId) {
        try {
            ItemRequestModel gtrm = new ItemRequestModel(orderId, SafeStorage.getUsername());
            Call<AcceptanceModel> call = api.requestDismissOrder(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){

                            loadDetailList(orderId);
                            view.showNotification("Order is now available for other delivery operators");
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

    /**
     * Request a number of available support operator to resolve a technical request/issue
     */
    @Override
    public void getSupportNumber(){
        try {
            LoginOnlyModel gtrm = new LoginOnlyModel(SafeStorage.getUsername());
            Call<NumberResponseModel> call = api.requestSupportNumber(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<NumberResponseModel>() {
                @Override
                public void onResponse(Call<NumberResponseModel> call, Response<NumberResponseModel> response) {
                    if (response.body() != null) {

                        NumberResponseModel irm = response.body();
                        if(irm.getError()==null||irm.getError().equals("null")){
                            view.openCaller(irm.getNumber());
                        }else{
                            view.showNotification(irm.getError());
                        }
                    } else {
                        view.showNotification("Server error");
                    }
                }

                @Override
                public void onFailure(Call<NumberResponseModel> call, Throwable t) {
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

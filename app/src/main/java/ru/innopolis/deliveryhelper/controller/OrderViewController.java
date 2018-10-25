package ru.innopolis.deliveryhelper.controller;

import android.graphics.Color;
import android.util.Pair;
import android.util.Log;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
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
                            view.setActionState(0);
                            view.setAssignedPanelState(0);
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

    @Override
    public void pickOrder(String orderId, String key) {
        //TODO
    }

    @Override
    public void validateRecipient(String orderId) {
        //TODO
    }

    @Override
    public void deliverOrder(String orderId, String key) {
        //TODO
    }

    @Override
    public void cancelOrder(String orderId) {
        //TODO
    }
}

package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.model.dataframes.request.LoginOnlyModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.AcceptanceModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.internal_server_error_message;
import static ru.innopolis.deliveryhelper.model.PlainConsts.logout_successful_message;

public class ContainerController implements ContainerMVC.Controller {


    private final String TAG = "ContainerController";
    private ContainerMVC.View view;
    private ApiInterface api;
    private Gson gson;

    /**
     * Constructor
     *
     * @param view Calling view
     */
    public ContainerController(ContainerMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    /**
     * Logout request to server, if successful then token will be deleted from server. Also delete all stored credentials from safe storage.
     */
    @Override
    public void logOut() {
        try {
            // construct request call object
            LoginOnlyModel gtrm = new LoginOnlyModel(SafeStorage.getUsername());
            //convert request object into json and send to server
            Call<AcceptanceModel> call = api.logout(SafeStorage.getToken(), RequestBody.create(MediaType.parse("application/json"), gson.toJson(gtrm)));
            call.enqueue(new Callback<AcceptanceModel>() {
                @Override
                public void onResponse(Call<AcceptanceModel> call, Response<AcceptanceModel> response) {
                    if (response.body() != null) {

                        AcceptanceModel irm = response.body();
                        if (irm.getError() == null || irm.getError().equals("null")) {
                            Log.d(TAG, logout_successful_message);
                        } else {
                            Log.e(TAG, irm.getError());
                        }
                    } else {
                        Log.e(TAG, internal_server_error_message);
                    }
                }

                @Override
                public void onFailure(Call<AcceptanceModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    call.cancel();
                }
            });
        } catch (Exception e) {
            view.showNotification(e.getMessage());
        }
        // delete credentials
        SafeStorage.clearCredentials();
        // get current location
        view.stopLocationService();
        // make transition to login activity
        view.returnToLoginActivity();
    }
}

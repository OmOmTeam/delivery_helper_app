package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import java.security.NoSuchAlgorithmException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.LoginResponseModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.ui.LoginActivity;

public class LoginController implements LoginMVC.Controller {

    private final String TAG = "LoginController";
    private LoginMVC.View view;
    private ApiInterface api;

    public LoginController(LoginMVC.View view) {
        this.view = view;
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    @Override
    public void tryLogin(final String login, String password) {
        //TODO: check login and password validity
        try {
            Call<LoginResponseModel> call = api.login(login, SafeStorage.hash(password));
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    if (response.body() != null) {
                        LoginResponseModel lrm = response.body();
                        if (lrm.getErrorMessage().contains("null") || lrm.getErrorMessage() == null) {
                            SafeStorage.setToken(lrm.getSessionToken());
                            view.showNotification(String.format("Token = %s", lrm.getSessionToken()));
                        } else {
                            view.showNotification(String.format("Error: %s", lrm.getErrorMessage()));
                        }
                    } else {
                        view.showNotification("Server error");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification(t.getMessage());
                    call.cancel();
                }
            });
        } catch (NoSuchAlgorithmException e) {

        }
    }
}

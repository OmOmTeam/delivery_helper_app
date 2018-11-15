package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;
import ru.innopolis.deliveryhelper.model.dataframes.request.LoginRequestModel;
import ru.innopolis.deliveryhelper.model.dataframes.response.LoginResponseModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.internal_server_error_message;

public class LoginController implements LoginMVC.Controller {

    private final String TAG = "LoginController";
    private LoginMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public LoginController(LoginMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    /**
     * Request user login with credentials, on success store the token in safe storage
     *
     * @param login    employee id
     * @param password password corresponding to given employee id
     */
    @Override
    public void tryLogin(final String login, final String password) {
        try {
            LoginRequestModel requestModel = new LoginRequestModel(login, SafeStorage.hash(password));
            Call<LoginResponseModel> call = api.login(RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestModel)));
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    if (response.body() != null) {
                        LoginResponseModel lrm = response.body();
                        if (lrm.getErrorMessage() == null || lrm.getErrorMessage().contains("null")) {
                            SafeStorage.setToken(lrm.getSessionToken());
                            SafeStorage.setUsername(login);
                            try {
                                SafeStorage.setPassword(SafeStorage.hash(password));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                                return;
                            }
                            view.goToOrderListActivity();
                        } else {
                            view.showNotification(String.format("<b>%s</b>", lrm.getErrorMessage()));
                        }
                    } else {
                        view.showNotification(internal_server_error_message);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    view.showNotification("Can't connect to server, <b>are you online?</b>");
                    call.cancel();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            view.showNotification(e.getMessage());
        }
    }
}

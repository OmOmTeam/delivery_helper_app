package ru.innopolis.deliveryhelper.controller;

import android.util.Log;

import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.ItemHeaderResponseModel;
import ru.innopolis.deliveryhelper.model.LoginRequestModel;
import ru.innopolis.deliveryhelper.model.LoginResponseModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;

public class ContainerController implements ContainerMVC.Controller {


    private final String TAG = "LoginController";
    private ContainerMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public ContainerController(ContainerMVC.View view) {
        this.view = view;
        gson = new Gson();
        api = RetrofitService.getInstance().create(ApiInterface.class);
    }

    @Override
    public void logOut() {
        SafeStorage.clearCredentials();
        view.returnToLoginActivity();
    }
}

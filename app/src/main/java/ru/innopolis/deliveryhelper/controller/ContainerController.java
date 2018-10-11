package ru.innopolis.deliveryhelper.controller;

import com.google.gson.Gson;

import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
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

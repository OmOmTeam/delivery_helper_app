package ru.innopolis.deliveryhelper.controller;

import com.google.gson.Gson;

import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;
import ru.innopolis.deliveryhelper.model.SafeStorage;

public class OrderListController implements OrderListMVC.Controller {


    private final String TAG = "LoginController";
    private OrderListMVC.View view;
    private ApiInterface api;
    private Gson gson;

    public OrderListController(OrderListMVC.View view) {
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

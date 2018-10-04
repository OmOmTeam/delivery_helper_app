package ru.innopolis.deliveryhelper.controller;

import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.ui.LoginActivity;

public class LoginController implements LoginMVC.Controller {

    private LoginMVC.View view;

    public LoginController(LoginMVC.View view) {
        this.view = view;
    }

    @Override
    public void tryLogin(String login, String password) {

    }
}

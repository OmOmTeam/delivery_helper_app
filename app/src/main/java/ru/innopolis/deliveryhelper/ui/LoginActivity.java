package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;

public class LoginActivity extends AppCompatActivity implements LoginMVC.View{

    private LoginMVC.Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controller = new LoginController(this);
    }

    @Override
    public void setCredentials() {

    }

    @Override
    public void showNotification(String message) {

    }

}

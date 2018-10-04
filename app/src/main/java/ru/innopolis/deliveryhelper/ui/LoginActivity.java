package ru.innopolis.deliveryhelper.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

import de.adorsys.android.securestoragelibrary.SecurePreferences;
import retrofit2.Call;
import retrofit2.Callback;
import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.LoginModel;
import ru.innopolis.deliveryhelper.model.RetrofitService;

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

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = ("admin" + ":" + "12345").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

//    public static void getHomePage(Callback<LoginModel> callback) {
//        Call<LoginModel> call = RetrofitService.getInstance().create(ApiInterface.class).login(
//                getAuthToken(),
//        );
//        call.enqueue(callback);
//    }
}

package ru.innopolis.deliveryhelper.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.adorsys.android.securestoragelibrary.SecurePreferences;
import retrofit2.Call;
import retrofit2.Callback;
import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;
import ru.innopolis.deliveryhelper.model.ApiInterface;
import ru.innopolis.deliveryhelper.model.RetrofitService;

public class LoginActivity extends AppCompatActivity implements LoginMVC.View{

    private LoginMVC.Controller controller;

    @BindView(R.id.master_login)
    TextView loginField;
    @BindView(R.id.master_password)
    TextView passwordField;
    @BindView(R.id.button_login)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controller = new LoginController(this);

        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String login = loginField.getText().toString().trim(), password = passwordField.getText().toString().trim();
                controller.tryLogin(login, password);
                showDismissableNotification("Hello, App!");
            }
        });
    }

    @Override
    public void setCredentials() {

    }

    @Override
    public void showNotification(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar mySnackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    //TODO: extend this method to interface
    public void showDismissableNotification(String message){
        View parentLayout = findViewById(android.R.id.content);
        final Snackbar snack = Snackbar.make(parentLayout, message, Snackbar.LENGTH_INDEFINITE);
        snack.setAction(R.string.CLOSE, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack.dismiss();
            }
        });
        snack.show();
    }

}


package ru.innopolis.deliveryhelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.LoginMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;
import ru.innopolis.deliveryhelper.model.SafeStorage;

import static android.graphics.Typeface.BOLD;

public class LoginActivity extends AppCompatActivity implements LoginMVC.View{

    private LoginMVC.Controller controller;

    @BindView(R.id.master_login)
    TextView loginField;
    @BindView(R.id.master_password)
    TextView passwordField;
    @BindView(R.id.ip_field)
    TextView ipField;
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
                String login = loginField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                String ip = ipField.getText().toString().trim();
                SafeStorage.setAddress(ip);
                controller = new LoginController(LoginActivity.this);

                controller.tryLogin(login, password);
                //showDismissableNotification("Attempting to login");
            }
        });
    }

    @Override
    public void setCredentials(String login, String password) {
        this.loginButton.setText(login);
        this.passwordField.setText(password);
    }

    @Override
    public void showNotification(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final TextView message = new TextView(this);
        message.setText(Html.fromHtml(text));
        message.setPadding(50,50,50,0);
        message.setTypeface(null, Typeface.NORMAL);
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        message.setTextColor(Color.BLACK);
        alert.setView(message);
        alert.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // nothing to do
            }
        });
        alert.show();
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

    public void goToOrderListActivity() {
        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}


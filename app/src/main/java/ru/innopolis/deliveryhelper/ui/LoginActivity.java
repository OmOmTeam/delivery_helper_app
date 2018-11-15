package ru.innopolis.deliveryhelper.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements LoginMVC.View {

    private static final String TAG = "LoginActivity";
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                String ip = ipField.getText().toString().trim();
                SafeStorage.setAddress(ip);
                controller = new LoginController(LoginActivity.this);

                controller.tryLogin(login, password);
            }
        });

        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, this)) {
            Log.i(TAG, "GPS Permissions not granted");
            ActivityCompat.requestPermissions(
                    this, // Activity
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
            Log.i(TAG, "GPS Permissions requested revoke");
        } else {
            Log.i(TAG, "GPS Permissions already granted");
        }
    }

    /**
     * Check if certain permissions are granted for the application.
     *
     * @param permission Selected permission
     * @param c          context
     * @return true if permission is granted, false otherwise
     */
    public static boolean isPermissionGranted(String permission, Context c) {
        return (ContextCompat.checkSelfPermission(c, permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Fill login and password field with given values
     *
     * @param login    value for login field
     * @param password value for password field
     */
    @Override
    public void setCredentials(String login, String password) {
        this.loginField.setText(login);
        this.passwordField.setText(password);
    }

    /**
     * Show notification on top of screen for user
     *
     * @param text Message in html format that user should see
     */
    @Override
    public void showNotification(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final TextView message = new TextView(this);
        message.setText(Html.fromHtml(text));
        message.setPadding(50, 50, 50, 0);
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

    /**
     * Make transition to order list activity
     */
    public void goToOrderListActivity() {
        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}


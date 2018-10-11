package ru.innopolis.deliveryhelper.model.dataframes.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel {
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String pasword;

    public LoginRequestModel(String login, String password) {
        this.login = login;
        this.pasword = password;
    }
}

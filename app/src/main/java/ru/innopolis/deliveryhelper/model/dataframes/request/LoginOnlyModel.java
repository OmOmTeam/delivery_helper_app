package ru.innopolis.deliveryhelper.model.dataframes.request;

import com.google.gson.annotations.SerializedName;

public class LoginOnlyModel {
    @SerializedName("login")
    private String login;

    public LoginOnlyModel(String login) {
        this.login = login;
    }
}

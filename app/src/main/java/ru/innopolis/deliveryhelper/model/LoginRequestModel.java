package ru.innopolis.deliveryhelper.model;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }
}

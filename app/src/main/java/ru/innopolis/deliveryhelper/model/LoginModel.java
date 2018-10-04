package ru.innopolis.deliveryhelper.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("non_field_errors")
    private String errorMessage;

    @SerializedName("token")
    private String sessionToken;

    @Override
    public String toString() {
        return "LoginResponseModel{" +
                "errorMessage='" + errorMessage + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

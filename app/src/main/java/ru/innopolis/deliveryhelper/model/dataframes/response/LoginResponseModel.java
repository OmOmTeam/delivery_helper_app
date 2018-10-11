package ru.innopolis.deliveryhelper.model.dataframes.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {
    @SerializedName("error")
    private String errorMessage;

    @SerializedName("token")
    private String sessionToken;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}

package ru.innopolis.deliveryhelper.model.dataframes.response;

import com.google.gson.annotations.SerializedName;

public class NumberResponseModel {
    @SerializedName("number")
    private String number;
    @SerializedName("error")
    private String error;

    public String getNumber() {
        return number;
    }

    public String getError() {
        return error;
    }
}

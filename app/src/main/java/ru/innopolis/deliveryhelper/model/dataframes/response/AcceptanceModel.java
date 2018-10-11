package ru.innopolis.deliveryhelper.model.dataframes.response;

import com.google.gson.annotations.SerializedName;

public class AcceptanceModel {

    @SerializedName("success")
    private boolean success;

    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}

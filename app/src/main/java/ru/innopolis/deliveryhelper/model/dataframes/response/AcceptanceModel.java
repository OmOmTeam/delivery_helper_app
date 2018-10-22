package ru.innopolis.deliveryhelper.model.dataframes.response;

import com.google.gson.annotations.SerializedName;

public class AcceptanceModel {
    @SerializedName("error")
    private String error;

    public String getError() {
        return error;
    }
}

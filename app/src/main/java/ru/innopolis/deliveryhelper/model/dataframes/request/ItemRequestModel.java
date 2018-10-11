package ru.innopolis.deliveryhelper.model.dataframes.request;

import com.google.gson.annotations.SerializedName;

public class ItemRequestModel {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("login")
    private String login;

    public ItemRequestModel(String orderId, String login) {
        this.orderId = orderId;
        this.login = login;
    }
}

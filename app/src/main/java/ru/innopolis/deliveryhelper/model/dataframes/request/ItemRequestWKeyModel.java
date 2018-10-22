package ru.innopolis.deliveryhelper.model.dataframes.request;

import com.google.gson.annotations.SerializedName;

public class ItemRequestWKeyModel {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("login")
    private String login;

    @SerializedName("key")
    private String key;

    public ItemRequestWKeyModel(String orderId, String login, String key) {
        this.orderId = orderId;
        this.login = login;
        this.key = key;
    }
}

package ru.innopolis.deliveryhelper.model;

import com.google.gson.annotations.SerializedName;

public class GetDetailsRequestModel {
    @SerializedName("order_id")
    private String orderId;

    public GetDetailsRequestModel(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

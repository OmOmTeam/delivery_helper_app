package ru.innopolis.deliveryhelper.model.dataframes.response;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class ItemHeaderResponseModel {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("title")
    private String title;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dimensions")
    private String dimensions;
    @SerializedName("warehouse_id")
    private String warehouseId;
    @SerializedName("warehouse_location")
    private String warehouseLocation;
    @SerializedName("recipient_location")
    private String recipientLocation;
    @SerializedName("order_type")
    private String orderType;

    public String getOrderId() {
        return orderId;
    }

    public String getTitle() {
        return title;
    }

    public String getWeight() {
        return weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public String getRecipientLocation() {
        return recipientLocation;
    }

    public String getOrderType() {
        return orderType;
    }
}

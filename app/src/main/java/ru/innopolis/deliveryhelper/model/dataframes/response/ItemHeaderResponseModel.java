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
    @SerializedName("state_code")
    private String stateCode;
    @SerializedName("delivery_time_from")
    private String deliveryTimeFrom;
    @SerializedName("delivery_time_to")
    private String deliveryTimeTo;

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

    public String getStateCode() {
        return stateCode;
    }

    public String getDeliveryTimeFrom() {
        return deliveryTimeFrom;
    }

    public String getDeliveryTimeTo() {
        return deliveryTimeTo;
    }
}

package ru.innopolis.deliveryhelper.model.dataframes.response;

import android.location.Location;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemResponseModel {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("title")
    private String title;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dimensions")
    private String dimensions;
    @SerializedName("state_code")
    private String stateCode;
    @SerializedName("state")
    private String state;
    @SerializedName("order_type")
    private String orderType;
    @SerializedName("warehouse_address")
    private String warehouseAddress;
    @SerializedName("recipient_address")
    private String recipientAddress;
    @SerializedName("warehouse_id")
    private String warehouseId;
    @SerializedName("warehouse_location")
    private String warehouseLocation;
    @SerializedName("recipient_location")
    private String recipientLocation;
    @SerializedName("delivery_time_from")
    private String deliveryTimeFrom;
    @SerializedName("delivery_time_to")
    private String deliveryTimeTo;
    @SerializedName("recipient_phone")
    private String recipientPhone;
    @SerializedName("recipient_name")
    private String recipientName;
    @SerializedName("assigned_to")
    private String assignedTo;
    @SerializedName("error")
    private String error;

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

    public String getStateCode() {
        return stateCode;
    }

    public String getState() {
        return state;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public String getRecipientAddress() {
        return recipientAddress;
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

    public String getDeliveryTimeFrom() {
        return deliveryTimeFrom;
    }

    public String getDeliveryTimeTo() {
        return deliveryTimeTo;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getError() {
        return error;
    }

    public List<Pair<String, String>> getAvailableParameters(){
        List<Pair<String, String>> notNullEntries = new ArrayList<>();
        if(orderId!=null){
            notNullEntries.add(new Pair<>("Order ID", orderId));
        }
        if(title!=null){
            notNullEntries.add(new Pair<>("Title", title));
        }
        if(weight!=null){
            notNullEntries.add(new Pair<>("Weight", weight));
        }
        if(dimensions!=null){
            notNullEntries.add(new Pair<>("Dimensions", dimensions));
        }
        if(state!=null){
            notNullEntries.add(new Pair<>("Status", state));
        }
        if(orderType!=null){
            notNullEntries.add(new Pair<>("Order Type", orderType));
        }
        if(warehouseAddress!=null){
            notNullEntries.add(new Pair<>("Warehouse Address", warehouseAddress));
        }
        if(recipientAddress!=null){
            notNullEntries.add(new Pair<>("Recipient Address", recipientAddress));
        }
        if(deliveryTimeFrom!=null){
            notNullEntries.add(new Pair<>("Delivery From", deliveryTimeFrom));
        }
        if(deliveryTimeTo!=null){
            notNullEntries.add(new Pair<>("Delivery To", deliveryTimeTo));
        }
        if(recipientName!=null){
            notNullEntries.add(new Pair<>("Recipient Name", recipientName));
        }
        if(recipientPhone!=null){
            notNullEntries.add(new Pair<>("Recipient Phone", recipientPhone));
        }
        if(assignedTo!=null){
            notNullEntries.add(new Pair<>("Assigned To", assignedTo));
        }
        return notNullEntries;
    }
}

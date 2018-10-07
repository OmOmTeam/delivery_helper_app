package ru.innopolis.deliveryhelper.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ItemResponseModel {
    @SerializedName("order_id")
    private String itemId;
    @SerializedName("order_type")
    private String itemType;
    @SerializedName("destination")
    private String destination;
    @SerializedName("address")
    private String destinationAddress;
    @SerializedName("description")
    private String description;
    @SerializedName("error")
    private String error;
    @SerializedName("dimensions")
    private String dimensions;
    @SerializedName("weight")
    private String weight;
    @SerializedName("delivery_state")
    private String deliveryState;
    @SerializedName("assigned_to")
    private String assignedTo;
    @SerializedName("delivery_time_from")
    private String deliveryTimeFrom;
    @SerializedName("deliveryT_time_to")
    private String deliveryTimeTo;
    @SerializedName("customer_phone")
    private String customerPhone;
    @SerializedName("customer_name")
    private String customerName;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDeliveryTimeFrom() {
        return deliveryTimeFrom;
    }

    public void setDeliveryTimeFrom(String deliveryTimeFrom) {
        this.deliveryTimeFrom = deliveryTimeFrom;
    }

    public String getDeliveryTimeTo() {
        return deliveryTimeTo;
    }

    public void setDeliveryTimeTo(String deliveryTimeTo) {
        this.deliveryTimeTo = deliveryTimeTo;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

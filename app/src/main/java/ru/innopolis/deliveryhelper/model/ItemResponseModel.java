package ru.innopolis.deliveryhelper.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ItemResponseModel {
    @SerializedName("item_id")
    private String itemId;
    @SerializedName("item_type")
    private Integer itemType;
    @SerializedName("destination")
    private Location destination;
    @SerializedName("address")
    private String destinationAddress;
    @SerializedName("description")
    private String description;
    @SerializedName("non_field_error")
    private String error;
    @SerializedName("dimensions")
    private Double[] dimensions;
    @SerializedName("weight")
    private Double weight;
    @SerializedName("scheduled_delivery_time")
    private Date scheduledDeliveryTime;
    @SerializedName("delivery_state")
    private Integer deliveryState;
    @SerializedName("assigned_to")
    private String assignedTo;

    public Double[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(Double[] dimensions) {
        this.dimensions = dimensions;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getScheduledDeliveryTime() {
        return scheduledDeliveryTime;
    }

    public void setScheduledDeliveryTime(Date scheduledDeliveryTime) {
        this.scheduledDeliveryTime = scheduledDeliveryTime;
    }

    public Integer getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(Integer deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}

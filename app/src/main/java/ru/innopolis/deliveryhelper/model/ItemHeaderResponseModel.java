package ru.innopolis.deliveryhelper.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class ItemHeaderResponseModel {
    @SerializedName("order_id")
    private String itemId;
    @SerializedName("title")
    private String title;
    @SerializedName("address")
    private String address;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dimensions")
    private String dimensions;
    @SerializedName("distance_from_warehouse")
    private String distanceFromWarehouse;
    @SerializedName("order_type")
    private String itemType;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDistanceFromWarehouse() {
        return distanceFromWarehouse;
    }

    public void setDistanceFromWarehouse(String distanceFromWarehouse) {
        this.distanceFromWarehouse = distanceFromWarehouse;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}

package ru.innopolis.deliveryhelper.model.dataframes.response;

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

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getWeight() {
        return weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getDistanceFromWarehouse() {
        return distanceFromWarehouse;
    }

    public String getItemType() {
        return itemType;
    }

}

package ru.innopolis.deliveryhelper.model.dataframes.request;

import com.google.gson.annotations.SerializedName;

public class LocationUpdateModel {
    @SerializedName("login")
    private String login;

    @SerializedName("location")
    private String location;

    public LocationUpdateModel(String login, String location) {
        this.login = login;
        this.location = location;
    }
}

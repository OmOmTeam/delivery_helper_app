package ru.innopolis.deliveryhelper.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class LocationMarker {

    @SerializedName("current_location")
    private Location currentLocation;


}

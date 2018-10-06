package ru.innopolis.deliveryhelper.ui;

public class OrderEntry {

    private String title;
    private String address;
    private String weight;
    private String dimensions;
    private String distance_from_warehouse;
    private int icon;

    public OrderEntry(String title, String address, String weight, String dimensions, String distance_from_warehouse, int icon) {
        this.title = title;
        this.address = address;
        this.weight = weight;
        this.dimensions = dimensions;
        this.distance_from_warehouse = distance_from_warehouse;
        this.icon = icon;
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

    public String getDistance_from_warehouse() {
        return distance_from_warehouse;
    }

    public int getIcon() {
        return icon;
    }
}

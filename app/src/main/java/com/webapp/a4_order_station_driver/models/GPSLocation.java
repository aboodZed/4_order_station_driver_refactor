package com.webapp.a4_order_station_driver.models;

public class GPSLocation {

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "GPSLocation{" +
                "Lat='" + lat + '\'' +
                ", Lng='" + lng + '\'' +
                '}';
    }
}
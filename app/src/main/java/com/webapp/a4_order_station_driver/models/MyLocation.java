package com.webapp.a4_order_station_driver.models;

public class MyLocation extends GPSLocation {

    private int driver_id;
    private String status;
    private String name;
    private String mobile;
    private int country_id;

    public MyLocation() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    @Override
    public String toString() {
        return "MyLocation{" +
                "driver_id=" + driver_id +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", country_id=" + country_id +
                '}';
    }
}

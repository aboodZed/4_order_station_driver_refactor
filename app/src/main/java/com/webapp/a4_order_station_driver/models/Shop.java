package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name_ar")
    @Expose
    private String name_ar;
    @SerializedName("name_en")
    @Expose
    private String name_en;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("cover_image")
    @Expose
    private String cover_image;
    @SerializedName("address_ar")
    @Expose
    private String address_ar;
    @SerializedName("address_en")
    @Expose
    private String address_en;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("start_work_at")
    @Expose
    private String start_work_at;
    @SerializedName("end_work_at")
    @Expose
    private String end_work_at;
    @SerializedName("content_id")
    @Expose
    private String content_id;
    @SerializedName("content_type")
    @Expose
    private String content_type;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;
    @SerializedName("parent_id")
    @Expose
    private String parent_id;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo_url")
    @Expose
    private String logo_url;
    @SerializedName("cover_image_url")
    @Expose
    private String cover_image_url;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("rate")
    @Expose
    private double rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getAddress_ar() {
        return address_ar;
    }

    public void setAddress_ar(String address_ar) {
        this.address_ar = address_ar;
    }

    public String getAddress_en() {
        return address_en;
    }

    public void setAddress_en(String address_en) {
        this.address_en = address_en;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getStart_work_at() {
        return start_work_at;
    }

    public void setStart_work_at(String start_work_at) {
        this.start_work_at = start_work_at;
    }

    public String getEnd_work_at() {
        return end_work_at;
    }

    public void setEnd_work_at(String end_work_at) {
        this.end_work_at = end_work_at;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}

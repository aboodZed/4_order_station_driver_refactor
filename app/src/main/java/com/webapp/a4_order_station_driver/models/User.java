package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webapp.a4_order_station_driver.utils.AppController;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("email_verified_at")
    @Expose
    private String email_verified_at;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("delivery_address")
    @Expose
    private String delivery_address;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("mobile_verify_code")
    @Expose
    private String mobile_verify_code;

    @SerializedName("reset_password_verify_code")
    @Expose
    private String reset_password_verify_code;

    @SerializedName("insurance_license")
    @Expose
    private String insurance_license;

    @SerializedName("drive_license")
    @Expose
    private String drive_license;

    @SerializedName("vehicle_license")
    @Expose
    private String vehicle_license;

    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;

    @SerializedName("vehicle_plate")
    @Expose
    private String vehicle_plate;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("id_pic")
    @Expose
    private String id_pic;

    @SerializedName("id_no")
    @Expose
    private String id_no;

    @SerializedName("complete_reg")
    @Expose
    private String complete_reg;

    @SerializedName("vehicle_pic")
    @Expose
    private String vehicle_pic;

    @SerializedName("is_online")
    @Expose
    private String is_online;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("country_id")
    @Expose
    private int country_id;

    @SerializedName("city_id")
    @Expose
    private int city_id;

    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;

    @SerializedName("Insurance_license_url")
    @Expose
    private String Insurance_license_url;

    @SerializedName("drive_license_url")
    @Expose
    private String drive_license_url;

    @SerializedName("vehicle_license_url")
    @Expose
    private String vehicle_license_url;

    @SerializedName("id_pic_url")
    @Expose
    private String id_pic_url;

    @SerializedName("vehicle_pic_url")
    @Expose
    private String vehicle_pic_url;

    @SerializedName("rate")
    @Expose
    private float rate;

    @SerializedName("password")
    private String password;

    @SerializedName("password_confirmation")
    private String password_confirmation;

    @SerializedName("Content-Type")
    private String content_type;

    //for first step in register
    public User(String name, String avatar, String mobile, String email,
                String address, String role, String password,
                String password_confirmation, int country_id ,int city_id) {
        this.name = name;
        this.avatar = avatar;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.country_id = country_id;
        this.city_id = city_id;
        content_type = "application/json";
    }

    //for second step in register
    public User(String vehicle_pic, String vehicle_license, String insurance_license,
                String id_pic, String drive_license, String vehicle_plate, String vehicle_type) {
        this.insurance_license = insurance_license;
        this.drive_license = drive_license;
        this.vehicle_license = vehicle_license;
        this.vehicle_plate = vehicle_plate;
        this.vehicle_type = vehicle_type;
        this.id_pic = id_pic;
        this.vehicle_pic = vehicle_pic;
        content_type = "application/json";
    }

    //for update the profile
    public User(String avatar,
                String insurance_license,
                String drive_license,
                String vehicle_license,
                String id_pic,
                String vehicle_pic,
                String password, String password_confirmation,
                String mobile, String email, String address) {
        this.avatar = avatar;
        this.insurance_license = insurance_license;
        this.drive_license = drive_license;
        this.vehicle_license = vehicle_license;
        this.id_pic = id_pic;
        this.vehicle_pic = vehicle_pic;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.country_id = AppController.getInstance().getAppSettingsPreferences().getCountry().getId();
        this.city_id = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getCity_id();
        content_type = "application/json";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMobile_verify_code() {
        return mobile_verify_code;
    }

    public void setMobile_verify_code(String mobile_verify_code) {
        this.mobile_verify_code = mobile_verify_code;
    }

    public String getReset_password_verify_code() {
        return reset_password_verify_code;
    }

    public void setReset_password_verify_code(String reset_password_verify_code) {
        this.reset_password_verify_code = reset_password_verify_code;
    }

    public String getInsurance_license() {
        return insurance_license;
    }

    public void setInsurance_license(String insurance_license) {
        this.insurance_license = insurance_license;
    }

    public String getDrive_license() {
        return drive_license;
    }

    public void setDrive_license(String drive_license) {
        this.drive_license = drive_license;
    }

    public String getVehicle_license() {
        return vehicle_license;
    }

    public void setVehicle_license(String vehicle_license) {
        this.vehicle_license = vehicle_license;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getId_pic() {
        return id_pic;
    }

    public void setId_pic(String id_pic) {
        this.id_pic = id_pic;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getComplete_reg() {
        return complete_reg;
    }

    public void setComplete_reg(String complete_reg) {
        this.complete_reg = complete_reg;
    }

    public String getVehicle_plate() {
        return vehicle_plate;
    }

    public void setVehicle_plate(String vehicle_plate) {
        this.vehicle_plate = vehicle_plate;
    }

    public String getVehicle_pic() {
        return vehicle_pic;
    }

    public void setVehicle_pic(String vehicle_pic) {
        this.vehicle_pic = vehicle_pic;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getInsurance_license_url() {
        return Insurance_license_url;
    }

    public void setInsurance_license_url(String insurance_license_url) {
        Insurance_license_url = insurance_license_url;
    }

    public String getDrive_license_url() {
        return drive_license_url;
    }

    public void setDrive_license_url(String drive_license_url) {
        this.drive_license_url = drive_license_url;
    }

    public String getVehicle_license_url() {
        return vehicle_license_url;
    }

    public void setVehicle_license_url(String vehicle_license_url) {
        this.vehicle_license_url = vehicle_license_url;
    }

    public String getId_pic_url() {
        return id_pic_url;
    }

    public void setId_pic_url(String id_pic_url) {
        this.id_pic_url = id_pic_url;
    }

    public String getVehicle_pic_url() {
        return vehicle_pic_url;
    }

    public void setVehicle_pic_url(String vehicle_pic_url) {
        this.vehicle_pic_url = vehicle_pic_url;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", active='" + active + '\'' +
                ", email_verified_at='" + email_verified_at + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", delivery_address='" + delivery_address + '\'' +
                ", lang='" + lang + '\'' +
                ", mobile_verify_code='" + mobile_verify_code + '\'' +
                ", reset_password_verify_code='" + reset_password_verify_code + '\'' +
                ", insurance_license='" + insurance_license + '\'' +
                ", drive_license='" + drive_license + '\'' +
                ", vehicle_license='" + vehicle_license + '\'' +
                ", vehicle_no='" + vehicle_no + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", id_pic='" + id_pic + '\'' +
                ", id_no='" + id_no + '\'' +
                ", complete_reg='" + complete_reg + '\'' +
                ", vehicle_plate='" + vehicle_plate + '\'' +
                ", vehicle_pic='" + vehicle_pic + '\'' +
                ", is_online='" + is_online + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", Insurance_license_url='" + Insurance_license_url + '\'' +
                ", drive_license_url='" + drive_license_url + '\'' +
                ", vehicle_license_url='" + vehicle_license_url + '\'' +
                ", id_pic_url='" + id_pic_url + '\'' +
                ", vehicle_pic_url='" + vehicle_pic_url + '\'' +
                '}';
    }
}

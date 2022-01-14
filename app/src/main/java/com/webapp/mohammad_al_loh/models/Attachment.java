package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachment implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("content_id")
    @Expose
    private String content_id;

    @SerializedName("content_type")
    @Expose
    private String content_type;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("is_default")
    @Expose
    private String is_default;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("image_url")
    @Expose
    private String image_url;

    public int getId() {
        return id;
    }

    public String getContent_id() {
        return content_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public String getName() {
        return name;
    }

    public String getIs_default() {
        return is_default;
    }

    public String getValue() {
        return value;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getImage_url() {
        return image_url;
    }
}

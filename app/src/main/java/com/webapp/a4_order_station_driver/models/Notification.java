package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification extends Message {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("notifiable_type")
    @Expose
    private String notifiable_type;

    @SerializedName("notifiable_id")
    @Expose
    private String notifiable_id;

    @SerializedName("data")
    @Expose
    private NotificationData data;

    @SerializedName("read_at")
    @Expose
    private String read_at;

    @SerializedName("created_at")
    @Expose
    private long created_at;

    public Notification() {
        super();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getNotifiable_type() {
        return notifiable_type;
    }

    public String getNotifiable_id() {
        return notifiable_id;
    }

    public NotificationData getData() {
        return data;
    }

    public String getRead_at() {
        return read_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", notifiable_type='" + notifiable_type + '\'' +
                ", notifiable_id='" + notifiable_id + '\'' +
                ", data=" + data +
                ", read_at='" + read_at + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}


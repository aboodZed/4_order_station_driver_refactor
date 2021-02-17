package com.webapp.a4_order_station_driver.models;

public class PublicChatMessage extends ChatMessage {

    private String imageUrl;

    public PublicChatMessage() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return "PublicChatMessage{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

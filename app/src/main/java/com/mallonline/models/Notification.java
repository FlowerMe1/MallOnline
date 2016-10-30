package com.mallonline.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dina on 19/09/2016.
 */

public class Notification {

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("picture")
    private String pictureUrl;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}

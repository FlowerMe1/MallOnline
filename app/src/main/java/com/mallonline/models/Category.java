package com.mallonline.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dina on 07/09/2016.
 */
@Table(name = "Category")
public class Category extends Model {

    @Column(name = "title")
    @SerializedName("title")
    private String title;

    @Column(name = "index_num")
    @SerializedName("index")
    private int index;

    @Column(name = "server_id")
    @SerializedName("id")
    private String serverId;

    @Column(name = "picture")
    @SerializedName("picture")
    private String picture;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

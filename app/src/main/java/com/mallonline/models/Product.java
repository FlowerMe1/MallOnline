package com.mallonline.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dina on 06/09/2016.
 */

public class Product implements Parcelable {

    public final static String PRODUCT_TITLE = "title";
    public final static String PRODUCT_DESCRIPTION = "description";
    public final static String PRODUCT_IMAGE = "picture";
    public final static String PRODUCT_IMAGES = "product_images";
    public final static String PRODUCT_PRICE = "price";
    public final static String PRODUCT_CATEGORY_SERVER_ID = "category_id";
    public final static String PRODUCT_SERVER_ID = "id";

    public Product(Parcel source) {
        try {
            Bundle data = source.readBundle();
            title = data.getString(PRODUCT_TITLE);
            description = data.getString(PRODUCT_DESCRIPTION);
            image = data.getString(PRODUCT_IMAGE);
            price = data.getDouble(PRODUCT_PRICE);
            categoryServerId = data.getString(PRODUCT_CATEGORY_SERVER_ID);
            images = data.getStringArrayList(PRODUCT_IMAGES);
            serverId = data.getString(PRODUCT_SERVER_ID);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("picture")
    private String image;

    @SerializedName("product_images")
    private ArrayList<String> images;

    @SerializedName("is_new")
    private boolean newArrival;

    @SerializedName("id")
    private String serverId;

    @SerializedName("price")
    private double price;

    @SerializedName("category_id")
    private String categoryServerId;

    public boolean isNewArrival() {
        return newArrival;
    }

    public void setNewArrival(boolean newArrival) {
        this.newArrival = newArrival;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryServerId() {
        return categoryServerId;
    }

    public void setCategoryServerId(String categoryServerId) {
        this.categoryServerId = categoryServerId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    /*Parcelable Methods.*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_TITLE, title);
        bundle.putString(PRODUCT_DESCRIPTION, description);
        bundle.putString(PRODUCT_IMAGE, image);
        bundle.putStringArrayList(PRODUCT_IMAGES, images);
        bundle.putDouble(PRODUCT_PRICE, price);
        bundle.putString(PRODUCT_CATEGORY_SERVER_ID, categoryServerId);
        bundle.putString(PRODUCT_SERVER_ID, serverId);
        dest.writeBundle(bundle);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel data) {
            return new Product(data);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

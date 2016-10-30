package com.mallonline.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dina on 10/09/2016.
 */

@Table(name = "CartOrder")
public class CartOrder extends Model {

    public CartOrder(){}

    public CartOrder(String userServerId, String productServerId) {
        this.userServerId = userServerId;
        this.productServerId = productServerId;
    }

    @Column(name = "product_id")
    @SerializedName("product_id")
    private String productServerId;

    @Column(name = "user_id")
    @SerializedName("user_id")
    private String userServerId;

    @Column(name = "in_card")
//    @SerializedName("in_card")
    private boolean inCard;


    public boolean isInCard() {
        return inCard;
    }

    public void setInCard(boolean inCard) {
        this.inCard = inCard;
    }

    public String getUserServerId() {
        return userServerId;
    }

    public void setUserId(String userServerId) {
        this.userServerId = userServerId;
    }

    public String getProductServerId() {
        return productServerId;
    }

    public void setProductId(String productServerId) {
        this.productServerId = productServerId;
    }

}

package com.mallonline.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mallonline.models.CartOrder;

import java.lang.reflect.Type;

/**
 * Created by Dina on 12/09/2016.
 */

public class CartOrderAdapter implements JsonSerializer<CartOrder> {

    @Override
    public JsonElement serialize(CartOrder cartOrder, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (cartOrder.getUserServerId() != null)
            jsonObject.addProperty("user_id", cartOrder.getUserServerId());

        if (cartOrder.getProductServerId() != null)
            jsonObject.addProperty("product_id", cartOrder.getProductServerId());

        return jsonObject;
    }

}

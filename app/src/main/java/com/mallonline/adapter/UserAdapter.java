package com.mallonline.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mallonline.models.User;

import java.lang.reflect.Type;

/**
 * Created by Dina on 05/09/2016.
 */

public class UserAdapter implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (user.getEmail() != null)
            jsonObject.addProperty("email", user.getEmail());

        if (user.getPassword() != null)
            jsonObject.addProperty("password", user.getPassword());

        if (user.getUserName() != null)
            jsonObject.addProperty("user_name", user.getPassword());

        if (user.getPhone() != null)
            jsonObject.addProperty("mobile", user.getPhone());
        return jsonObject;
    }

}

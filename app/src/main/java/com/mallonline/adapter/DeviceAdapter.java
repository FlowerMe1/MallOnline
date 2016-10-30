package com.mallonline.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mallonline.models.Device;

import java.lang.reflect.Type;

/**
 * Created by Dina on 17/09/2016.
 */

public class DeviceAdapter implements JsonSerializer<Device> {

    @Override
    public JsonElement serialize(Device device, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (device.getOsName() != null)
            jsonObject.addProperty("os_name", device.getOsName());

        if (device.getUserServerId() != null)
            jsonObject.addProperty("user_id", device.getUserServerId());

        if (device.getDeviceName() != null)
            jsonObject.addProperty("device_name", device.getDeviceName());


        jsonObject.addProperty("os_version", device.getOsVersion());

        if (device.getRegId() != null)
            jsonObject.addProperty("reg_id", device.getRegId());
        return jsonObject;
    }

}

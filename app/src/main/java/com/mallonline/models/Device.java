package com.mallonline.models;

import android.os.Build;

import com.google.gson.annotations.SerializedName;
import com.mallonline.manager.UsersManager;

/**
 * Created by Dina on 17/09/2016.
 */

public class Device {

    public Device() {
        osName = "android";
        userServerId = UsersManager.getInstance().getCurrentUser().getServerId();
        deviceName = findDeviceName();
    }

    @SerializedName("os_name")
    private String osName;

    @SerializedName("reg_id")
    private String regId;

    @SerializedName("user_id")
    private String userServerId;

    @SerializedName("os_version")
    private int osVersion;

    @SerializedName("device_name")
    private String deviceName;

    /*getters and setter*/
    public int getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(int osVersion) {
        this.osVersion = osVersion;
    }

    public String getUserServerId() {
        return userServerId;
    }

    public void setUserServerId(String userServerId) {
        this.userServerId = userServerId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    private String findDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String retName = "";

        if (manufacturer != null && !"".equals(manufacturer))
            retName += capitalize(manufacturer) + " ";

        if (model != null && !"".equals(model))
            retName += capitalize(model);

        return retName;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}

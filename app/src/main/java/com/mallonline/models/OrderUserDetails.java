package com.mallonline.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dina on 12/09/2016.
 */

public class OrderUserDetails {

    //    @Column(name = "full_name")
    @SerializedName("full_name")
    private String fullName;

    //    @Column(name = "identity")
    @SerializedName("identity")
    private String identity;

    //    @Column(name = "phone")
    @SerializedName("phone")
    private String phone;

    //    @Column(name = "alternative_phone")
    @SerializedName("alternative_phone")
    private String alternativePhone;

    //    @Column(name = "country")
    @SerializedName("country")
    private String country;

    //    @Column(name = "full_adress")
    @SerializedName("address")
    private String fullAddress;

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserData(String fullName, String identity, String phone, String alternativePhone, String country, String fullAddress) {
        this.fullName = fullName;
        this.identity = identity;
        this.phone = phone;
        this.alternativePhone = alternativePhone;
        this.country = country;
        this.fullAddress = fullAddress;
    }

    /* User Information Validations*/
    public boolean isValidFullName() {
        if (fullName == null || "".equals(fullName.trim()))
            return false;
        return true;
    }

    public boolean isValidIdentity() {
        if (identity == null || "".equals(identity.trim()))
            return false;
        return true;
    }

    public boolean isValidPhone() {
        if (phone == null || "".equals(phone.trim()))
            return false;
        return true;
    }

    public boolean isValidAlternativePhone() {
        if (alternativePhone == null || "".equals(alternativePhone.trim()))
            return false;
        return true;
    }

    public boolean isValidFullAddress() {
        if (fullAddress == null || "".equals(fullAddress.trim()))
            return false;
        return true;
    }
}

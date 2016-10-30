package com.mallonline.models;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;
import com.mallonline.api.ApiClient;
import com.mallonline.util.UtilityMethods;

/**
 * Created by Dina on 05/09/2016.
 */

@Table(name = "User")
public class User extends Model implements Observable {

    @Column(name = "email")
    @SerializedName(ApiClient.USER_EMAIL)
    private
    @Bindable
    String email;

    @Column(name = "password")
    @SerializedName(ApiClient.USER_PASSWORD)
    private String password;

    @Column(name = "ServerId", unique = true)
    @SerializedName("id")
    private String serverId;

    @SerializedName("access_token")
    private String token;

    @SerializedName("user_name")
    @Column(name = "user_name")
    private String userName;

    @SerializedName("phone")
    @Column(name = "phone")
    private String phone;

    @Column(name = "isLogin")
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    PropertyChangeRegistry mRegistry = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.remove(callback);
    }

    public void setUserData(String email, String password) {
        this.email = email;
        if (password != null)
            this.password = password;
    }


    public void setUserData(String email, String password, String userName, String phone) {
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        if (password != null)
            this.password = password;
    }

    public Long saveUser() {
        return save();
    }

    /* User Information Validations*/
    public boolean isValidEmail() {
        if (email == null || "".equals(email.trim()) || !UtilityMethods.isEmailValid(email))
            return false;
        return true;
    }

    public boolean isValidPassword() {
        if (password == null || "".equals(password.trim()))
            return false;
        return true;
    }

    public boolean isValidUsername() {
        if (userName == null || "".equals(userName.trim()))
            return false;
        return true;
    }

    public boolean isValidPhone() {
        if (phone == null || "".equals(phone.trim()))
            return false;
        return true;
    }

}

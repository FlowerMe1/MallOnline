package com.mallonline.api;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mallonline.adapter.CartOrderAdapter;
import com.mallonline.adapter.DeviceAdapter;
import com.mallonline.adapter.UserAdapter;
import com.mallonline.data.SharedPref;
import com.mallonline.models.CartOrder;
import com.mallonline.models.Device;
import com.mallonline.models.OrderUserDetails;
import com.mallonline.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Dina on 05/09/2016.
 */

public class ApiClientImp extends VolleyHelper implements ApiClient {

    private static ApiClientImp instance;
    private String baseUrl;

    public static ApiClientImp getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClientImp(context);
        }
        return instance;
    }

    private ApiClientImp(Context context) {
        super(context);
        baseUrl = "https://mallonline.herokuapp.com";//"https://mallonline.herokuapp.com";//context.getResources().getString(R.string.base_url);
    }

    @Override
    public void userLogin(User user, ClientCallback callback) {
        Gson gson = customizeGsonBuilder().registerTypeAdapter(User.class,
                new UserAdapter()).create();

        String jsonStr = gson.toJson(user);
        JSONObject userJsonObj;
        JSONObject paramsJsonObj = new JSONObject();
        try {
            userJsonObj = new JSONObject(jsonStr);
            paramsJsonObj.put(USER, userJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduleJsonObjectRequest(Request.Method.POST, baseUrl + USER_SIGNIN_URL,
                paramsJsonObj, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void userSignup(User user, ClientCallback callback) {
        Gson gson = customizeGsonBuilder().registerTypeAdapter(User.class,
                new UserAdapter()).create();

        String jsonStr = gson.toJson(user);
        JSONObject userJsonObj;
        JSONObject paramsJsonObj = new JSONObject();
        try {
            userJsonObj = new JSONObject(jsonStr);
            userJsonObj.put("password_confirmation", user.getPassword());
            paramsJsonObj.put(USER, userJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduleJsonObjectRequest(Request.Method.POST, baseUrl + USER_SIGNUP_URL, paramsJsonObj, callback, JSONObject.class, NORMAL_TIME_OUT);

    }

    @Override
    public void getCategories(ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        scheduleJsonObjectRequest(Request.Method.GET, baseUrl + CATEGORIES + "?access_token=" + token, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void getProducts(List<String> productsIdsList, ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        JSONArray productsJsonArray = new JSONArray(productsIdsList);
        JSONObject productsJsonObject = new JSONObject();
        try {
            productsJsonObject.put("products_ids", productsJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        scheduleJsonObjectRequest(Request.Method.GET, baseUrl + GET_PRODUCTS + "?access_token=" + token + "&products_ids=" + productsJsonArray.toString(), productsJsonObject, callback, JSONObject.class, NORMAL_TIME_OUT);

    }

    @Override
    public void addCartOrders(List<CartOrder> cartOrdersList, OrderUserDetails orderUserDetails, ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        Gson orderGson = customizeGsonBuilder().registerTypeAdapter(CartOrder.class,
                new CartOrderAdapter()).create();
        String json = orderGson.toJson(cartOrdersList);
        JSONArray cartOrdersJsonArray = new JSONArray();
        try {
            cartOrdersJsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String userDetailsJsonStr = gson.toJson(orderUserDetails);
        JSONObject userJsonObj;
        JSONObject paramsJsonObj = new JSONObject();

        try {
            userJsonObj = new JSONObject(userDetailsJsonStr);
            paramsJsonObj.put(ORDER_USER_DETAILS, userJsonObj);
            paramsJsonObj.put(ORDERS, cartOrdersJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        scheduleJsonObjectRequest(Request.Method.POST, baseUrl + CREATE_ORDERS + "?access_token=" + token, paramsJsonObj, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void getProductsByCategory(String categoryServerId, ClientCallback callback, int page) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        scheduleJsonObjectRequest(Request.Method.GET, baseUrl + PRODUCT_URL + "?access_token=" + token + "&category=" + categoryServerId + "&page=" + page, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void newProductArrivals(ClientCallback callback, int page) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        scheduleJsonObjectRequest(Request.Method.GET, baseUrl + NEW_ARRIVALS + "?access_token=" + token + "&page=" + page, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void createDevice(Device device, ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        Gson gson = customizeGsonBuilder().registerTypeAdapter(Device.class,
                new DeviceAdapter()).create();

        String jsonStr = gson.toJson(device);
        JSONObject deviceJsonObj;
        JSONObject paramsJsonObj = new JSONObject();
        try {
            deviceJsonObj = new JSONObject(jsonStr);
            paramsJsonObj.put("device", deviceJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduleJsonObjectRequest(Request.Method.POST, baseUrl + DEVICES + "?access_token=" + token, paramsJsonObj, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void deleteDeviceWithRegId(String registrationId, ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        scheduleJsonObjectRequest(Request.Method.DELETE, baseUrl + DESTROY_DEVICE_WITH_REG_ID + "?access_token=" + token + "&" + USER_REG_ID + "=" + registrationId, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void getTodayNotifications(ClientCallback callback, int page) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        scheduleJsonObjectRequest(Request.Method.GET, baseUrl + GET_TODAY_NOTIFICATIONS + "?access_token=" + token + "&page=" + page, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }

    @Override
    public void searchProduct(String productCategoryServerId, String productTitle, int page, ClientCallback callback) {
        String token = SharedPref.LoadString(context, SharedPref.USER_TOKEN);
        String url = baseUrl + SEARCH_PRODUCT + "?access_token=" + token + "&page=" + page;
        try {
            url += "&search_text=" + URLEncoder.encode(productTitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            url += "&search_text=" + productTitle;
        }
        if (productCategoryServerId != null && !"".equals(productCategoryServerId.trim()))
            url += "&category_id=" + productCategoryServerId;
        scheduleJsonObjectRequest(Request.Method.GET, url, null, callback, JSONObject.class, NORMAL_TIME_OUT);
    }
}

package com.mallonline.api;

import com.mallonline.models.CartOrder;
import com.mallonline.models.Device;
import com.mallonline.models.OrderUserDetails;
import com.mallonline.models.User;

import java.util.List;

/**
 * Created by Dina on 05/09/2016.
 */

public interface ApiClient {

    /*User fields*/
    String USER = "user";
    String USER_EMAIL = "email";
    String USER_PASSWORD = "password";
    String USER_NAME = "name";
    String PHONE = "phone";

    /*Device fields*/
    String USER_REG_ID = "reg_id";

    /*Order fields*/
    String ORDER_USER_DETAILS = "user_data";
    String ORDERS = "orders";

    /*urls*/
    String USER_URL = "/users";
    String USER_SIGNIN_URL = USER_URL + "/sign_in";
    String USER_SIGNUP_URL = USER_URL;
    String PRODUCT_URL = "/products";
    String NEW_ARRIVALS = "/products/new";
    String CATEGORIES = "/categories";
    String GET_PRODUCTS = "/get_products";
    String CREATE_ORDERS = "/orders";
    String DEVICES = "/devices";
    String DESTROY_DEVICE_WITH_REG_ID = DEVICES + "/destroy_with_reg_id";
    String GET_TODAY_NOTIFICATIONS = "/today_notifications";
    String SEARCH_PRODUCT = "/search_product";

    void userLogin(User user, ClientCallback callback);

    void userSignup(User user, ClientCallback callback);

    void newProductArrivals(ClientCallback clientCallback, int page);

    void getCategories(ClientCallback callback);

    void getProducts(List<String> productsIdsList, ClientCallback callback);

    void addCartOrders(List<CartOrder> cartOrdersList, OrderUserDetails orderUserDetails, ClientCallback callback);

    void getProductsByCategory(String categoryServerId, ClientCallback callback, int page);

    void createDevice(Device device, ClientCallback callback);

    void deleteDeviceWithRegId(String registrationId, ClientCallback callback);

    void getTodayNotifications(ClientCallback callback, int page);

    void searchProduct(String productCategoryServerId, String productTitle, int page, ClientCallback callback);

}

package com.mallonline.manager;

import com.activeandroid.query.Select;
import com.mallonline.models.CartOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dina on 10/09/2016.
 */

public class OrdersManager {

    public final static String ORDER_PRODUCT_SERVER_ID = "product_id";
    public final static String ORDER_USER_SERVER_ID = "user_id";
    public final static String IN_CART = "in_card";

    private static OrdersManager _instance;

    public static OrdersManager getInstance() {
        if (_instance == null)
            _instance = new OrdersManager();
        return _instance;
    }

    public void addToCard(String userServerId, String productServerId) {
        CartOrder order = new CartOrder(userServerId, productServerId);
        order.setInCard(true);
        order.save();
    }

    public void removeFromCart(String userServerId, String productServerId) {
        CartOrder cartOrder = findOrderByServerId(userServerId, productServerId);
        if (cartOrder != null)
        cartOrder.delete();
    }

    public CartOrder findOrderByServerId(String userServerId, String productServerId) {
        return new Select().from(CartOrder.class).where(ORDER_PRODUCT_SERVER_ID + " = ? and " + ORDER_USER_SERVER_ID + " = ? ", productServerId, userServerId).executeSingle();
    }

    public boolean isAddedToCart(String userServerId, String productServerId) {
        return new Select().from(CartOrder.class).where(IN_CART + " = ? and  " + ORDER_PRODUCT_SERVER_ID + " = ? and " + ORDER_USER_SERVER_ID + " = ?", true, productServerId, userServerId).count() > 0;
    }

    public List<String> getCartProductsIds(String userServerId) {
        List<String> productsIds = new ArrayList<>();
        List<CartOrder> ordersList = new Select().from(CartOrder.class).where(IN_CART + " = ? and " + ORDER_USER_SERVER_ID + " = ?", true, userServerId).execute();
        for (CartOrder cartOrder :
                ordersList) {
            productsIds.add(cartOrder.getProductServerId());
        }
        return productsIds;
    }

    public List<CartOrder> getOrdersList(String userServerId) {
        List<CartOrder> ordersList = new Select().from(CartOrder.class).where(IN_CART + " = ? and " + ORDER_USER_SERVER_ID + " = ?", true, userServerId).execute();
        return ordersList;
    }

    public List<CartOrder> getAllCardOrders() {
        List<CartOrder> orders = new Select().from(CartOrder.class).where("inCard = ?", true).execute();
        return orders;
    }

}

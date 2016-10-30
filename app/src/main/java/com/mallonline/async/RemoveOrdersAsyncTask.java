package com.mallonline.async;

import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.mallonline.listeners.OnCompletedListener;
import com.mallonline.manager.OrdersManager;
import com.mallonline.models.CartOrder;

import java.util.List;

/**
 * Created by Dina on 12/09/2016.
 */

public class RemoveOrdersAsyncTask extends AsyncTask {

    private List<CartOrder> cartOrderList;
    private OrdersManager ordersManager;
    private OnCompletedListener onCompletedListener;

    public RemoveOrdersAsyncTask(List<CartOrder> cartOrderList, OnCompletedListener onCompletedListener) {
        this.cartOrderList = cartOrderList;
        this.ordersManager = OrdersManager.getInstance();
        this.onCompletedListener = onCompletedListener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ActiveAndroid.beginTransaction();
        for (CartOrder cartOrder :
                cartOrderList) {
            ordersManager.removeFromCart(cartOrder.getUserServerId(), cartOrder.getProductServerId());
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        onCompletedListener.onCompleted();
    }
}

package com.mallonline.async;

import android.os.AsyncTask;

import com.mallonline.listeners.CartTotalPriceListener;
import com.mallonline.models.Product;

import java.util.List;

/**
 * Created by Dina on 11/09/2016.
 */

public class CalculateTotalPriceAsyncTask extends AsyncTask {

    private List<Product> productList;
    private double totalPrice = 0;
    private CartTotalPriceListener listener;

    public CalculateTotalPriceAsyncTask(List<Product> productsList, CartTotalPriceListener listener) {
        this.productList = productsList;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(listener != null)
            listener.getTotalPrice(totalPrice);

    }
}

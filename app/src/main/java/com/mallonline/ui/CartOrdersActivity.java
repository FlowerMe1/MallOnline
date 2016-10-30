
package com.mallonline.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mallonline.R;
import com.mallonline.adapter.CartAdapter;
import com.mallonline.api.ClientCallback;
import com.mallonline.async.CalculateTotalPriceAsyncTask;
import com.mallonline.databinding.ActivityCartOrdersBinding;
import com.mallonline.listeners.CartTotalPriceListener;
import com.mallonline.listeners.ItemRemovedListener;
import com.mallonline.manager.OrdersManager;
import com.mallonline.manager.UsersManager;
import com.mallonline.models.Product;
import com.mallonline.models.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartOrdersActivity extends BaseActivity implements ClientCallback<JSONObject>, CartTotalPriceListener, ItemRemovedListener {

    private ActivityCartOrdersBinding binding;
    private List<Product> productList;
    private CartAdapter adapter;
    private OrdersManager ordersManager;
    private UsersManager usersManager;
    private Gson gson;
    private boolean isRefreshHome = false;
    private GridLayoutManager layoutManager;

    public static final int LANDSCAPE_COLS_NUM = 2, PORTRAIT_COLS_NUM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_cart_orders, null, false);
        gson = new Gson();
        inflateContent(binding.getRoot());
        setToolBarContent(getString(R.string.cart_orders_title), true, true);
        isShowHome(true);
        initRecyclerView();
        initProductsList();
        initOrderNowClick();
    }

    private void initOrderNowClick() {
        binding.buttonOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateActivity(new Intent(CartOrdersActivity.this, OrderUserDetailsActivity.class));
            }
        });
    }

    private void initProductsList() {
        ordersManager = OrdersManager.getInstance();
        usersManager = UsersManager.getInstance();
        List<String> productsIdsList = ordersManager.getCartProductsIds(usersManager.getCurrentUser().getServerId());
        if (productsIdsList != null && productsIdsList.size() > 0) {
            apiClient.getProducts(productsIdsList, this);
            showProgressDialog(getString(R.string.please_Wait), false, true);
        } else
            binding.totalCardView.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        this.productList = new ArrayList<>();
        RecyclerView recyclerView = binding.productsRecyclerView;
        layoutManager = new GridLayoutManager(this, getColsNum());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(productList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {
        Type listType = new TypeToken<List<Product>>() {
        }.getType();
        productList = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("products");
            productList = gson.fromJson(String.valueOf(jsonArray), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (productList != null) {
            adapter.setItems(productList);
            new CalculateTotalPriceAsyncTask(productList, this).execute();
        }
        if (!isFinishing())
            dismissProgressDialog();
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        showFailureMessage(serverResponse, statusCode);
    }

    @Override
    public void getTotalPrice(double totalPrice) {
        if (!isFinishing()) {
            if (totalPrice > 0)
                binding.totalTextView.setText(String.valueOf(totalPrice) + " " + getString(R.string.currency_sign));
            else
                binding.totalCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyItemRemoved() {
        new CalculateTotalPriceAsyncTask(adapter.getItems(), this).execute();
        Intent intent = new Intent(HomeActivity.ORDERS_STATE_CHANGED);
        intent.setAction(HomeActivity.ORDERS_STATE_CHANGED);
        sendBroadcast(intent);
    }

    //ToDo: remove duplicate code between this and home activity.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRecyclerViewColsNum();
        binding.productsRecyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }


    private int getColsNum() {
        int columnsNum = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? LANDSCAPE_COLS_NUM : PORTRAIT_COLS_NUM;
        return columnsNum;
    }

    private void setRecyclerViewColsNum() {
        if (productList != null && productList.size() == 0)
            layoutManager.setSpanCount(PORTRAIT_COLS_NUM);
        else
            layoutManager.setSpanCount(getColsNum());
    }
}

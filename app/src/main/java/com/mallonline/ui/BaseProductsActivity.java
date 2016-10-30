package com.mallonline.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;

import com.google.gson.reflect.TypeToken;
import com.mallonline.R;
import com.mallonline.adapter.NewProductsAdapter;
import com.mallonline.api.ClientCallback;
import com.mallonline.databinding.ActivityHomeBinding;
import com.mallonline.listeners.ItemSelectedListener;
import com.mallonline.listeners.OrderCartStateListener;
import com.mallonline.models.Product;
import com.mallonline.models.ServerResponse;
import com.mallonline.util.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dina on 01/10/2016.
 */

public class BaseProductsActivity extends CategoriesBaseActivity implements SwipeRefreshLayout.OnRefreshListener, ItemSelectedListener, ClientCallback<JSONObject>, OrderCartStateListener {

    protected boolean isLoading = false;
    protected NewProductsAdapter productsAdapter;
    protected ActivityHomeBinding binding;
    public static final int LANDSCAPE_COLS_NUM = 2, PORTRAIT_COLS_NUM = 2;
    protected StaggeredGridLayoutManager layoutManager;
    protected SwipeRefreshLayout productsSwipeContainer;
    protected List<Product> productList;
    protected int page = 1, per = 20;
    protected boolean lastPage, loading = false;
    protected int selectedSpinnerPosition = 0;
    protected boolean isNewArrivals = true;//////ToDo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_home, null, false);
        this.productsSwipeContainer = binding.productsSwipeContainer;
        productList = new ArrayList<>();
        isLoading = true;
        binding.productsSwipeContainer.setOnRefreshListener(this);
        initRecyclerView(binding.productsRecyclerView);
    }


    protected void initRecyclerView(RecyclerView recyclerView) {
        this.productList = new ArrayList<>();
        layoutManager = new StaggeredGridLayoutManager(getRecyclerviewColsNum(), StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        productsAdapter = new NewProductsAdapter(productList, this, this, isNewArrivals, this);
        recyclerView.setAdapter(productsAdapter);
        recyclerView.setItemAnimator(null);
//        apiClient.newProductArrivals(this, page);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollRecyclerView();
            }
        });
    }

    private void scrollRecyclerView() {
        int visibleItemCount = binding.productsRecyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPositions(null)[0];

        final int lastItem = firstVisibleItem + visibleItemCount;
        if (!lastPage && !loading && (lastItem) == totalItemCount && lastItem == (page * per) && (lastItem != 0)) {
            page++;
            requestProducts(selectedSpinnerPosition);
        }
    }

    protected void requestProducts(int position) {
        if (!isFinishing())
            binding.productsSwipeContainer.setRefreshing(true);
        if (position == 0) {
            isNewArrivals = true;
            apiClient.newProductArrivals(BaseProductsActivity.this, page);
        } else {
            isNewArrivals = false;
            String selectedCategoryServerId = categoryItems.get(position).getServerId();
            apiClient.getProductsByCategory(selectedCategoryServerId, BaseProductsActivity.this, page);
        }
        productsAdapter.setFilterNewArrivals(isNewArrivals);
    }

    //todo
    protected void loadPaginationFeeds() {
    }

    protected void setRecyclerViewAdapter() {
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestProducts(spinner.getSelectedItemPosition());
    }

    @Override
    public void onItemSelected(Object object) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.PRODUCT_DETAILS, ((Product) object));
        navigateActivity(intent);
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
            setRecyclerViewColsNum();
            if (page == 1)
                productsAdapter.setItems(productList);
            else
                productsAdapter.addItems(productList);
        }
        if (!isFinishing()) {
            dismissProgressDialog();
            binding.productsSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        String errorMessage = getString(R.string.something_wrong);
        if (serverResponse != null && serverResponse.getErrorMessageStr() != null && !"".equals((serverResponse.getErrorMessageStr()).trim()))
            errorMessage = serverResponse.getErrorMessageStr();

        if (!isFinishing()) {
            UtilityMethods.showSnackbar(errorMessage, binding.getRoot(), this);
            dismissProgressDialog();
            binding.productsSwipeContainer.setRefreshing(false);
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        setRecyclerViewColsNum();
//        binding.productsRecyclerView.setLayoutManager(layoutManager);
//        productsAdapter.notifyDataSetChanged();
//    }


    private int getColsNum() {
        int columnsNum = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? getLandscapeColsNum() : getPortraitColsNum();
        return columnsNum;
    }

    protected void setRecyclerViewColsNum() {
        layoutManager.setSpanCount(getRecyclerviewColsNum());
    }

    private int getRecyclerviewColsNum() {
        if (productList != null && productList.size() == 0)
            return 1;
        else
            return getColsNum();
    }

    @Override
    protected void addSearchOption(Menu menu) {
        super.addSearchOption(menu);
    }

    @Override
    public void onCartStateChanged() {

    }

    protected int getLandscapeColsNum() {
        return LANDSCAPE_COLS_NUM;
    }

    protected int getPortraitColsNum() {
        return PORTRAIT_COLS_NUM;
    }
}

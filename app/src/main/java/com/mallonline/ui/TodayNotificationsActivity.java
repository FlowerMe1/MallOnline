package com.mallonline.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mallonline.R;
import com.mallonline.adapter.NotificationAdapter;
import com.mallonline.api.ClientCallback;
import com.mallonline.databinding.ActivityHomeBinding;
import com.mallonline.dialog.NotificationDetailsDialog;
import com.mallonline.listeners.ItemSelectedListener;
import com.mallonline.models.Notification;
import com.mallonline.models.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TodayNotificationsActivity extends BaseActivity implements ClientCallback<JSONObject>, SwipeRefreshLayout.OnRefreshListener, ItemSelectedListener {

    private ActivityHomeBinding binding;
    private List<Notification> notificationList;
    private StaggeredGridLayoutManager layoutManager;
    private NotificationAdapter adapter;
    private Gson gson;
    private boolean loading = false;
    private int page = 1, per = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_home, null, false);
        inflateContent(binding.getRoot());
        initRecyclerView();
        apiClient.getTodayNotifications(this, page);
        setToolBarContent(getString(R.string.today_notifications), true, true);
        showProgressDialog(getString(R.string.please_Wait), false, true);
    }

    private void initRecyclerView() {
        gson = new Gson();
        this.notificationList = new ArrayList<>();
        RecyclerView recyclerView = binding.productsRecyclerView;
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(notificationList, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        binding.productsSwipeContainer.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollRecyclerView();
            }
        });
        binding.productsSwipeContainer.setRefreshing(true);
    }

    private void scrollRecyclerView() {
        int visibleItemCount = binding.productsRecyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPositions(null)[0];

        final int lastItem = firstVisibleItem + visibleItemCount;
        if (!loading && (lastItem) == totalItemCount && lastItem == (page * per) && (lastItem != 0)) {
            page++;
            requestNotifications();
        }
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {
        Type listType = new TypeToken<List<Notification>>() {
        }.getType();
        List<Notification> notificationList = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("notifications");
            notificationList = gson.fromJson(String.valueOf(jsonArray), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (notificationList != null) {
            if (page == 1)
                adapter.setItems(notificationList);
            else
                adapter.addItems(notificationList);
        }
        if (!isFinishing()) {
            dismissProgressDialog();
            binding.productsSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        showFailureMessage(serverResponse, statusCode);
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestNotifications();
    }

    private void requestNotifications() {
        if (!isFinishing())
            binding.productsSwipeContainer.setRefreshing(true);
        apiClient.getTodayNotifications(TodayNotificationsActivity.this, page);
    }

    @Override
    public void onItemSelected(Object item) {
        new NotificationDetailsDialog(this, (Notification) item).show();
    }

    private int getColsNum() {
        int columnsNum = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? BaseProductsActivity.LANDSCAPE_COLS_NUM : BaseProductsActivity.PORTRAIT_COLS_NUM;
        return columnsNum;
    }

    private void setRecyclerViewColsNum() {
        if (adapter != null && adapter.getItems() != null && adapter.getItems().size() == 0)
            layoutManager.setSpanCount(BaseProductsActivity.PORTRAIT_COLS_NUM);
        else
            layoutManager.setSpanCount(getColsNum());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRecyclerViewColsNum();
    }
}

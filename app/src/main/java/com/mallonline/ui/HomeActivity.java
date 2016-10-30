package com.mallonline.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mallonline.R;
import com.mallonline.async.RegisterGCMAsyncTask;

public class HomeActivity extends BaseProductsActivity {

    private RelativeLayout customToolbarLayout;
    private BroadcastReceiver broadcastReceiver;
    public final static String ORDERS_STATE_CHANGED = "ORDER_STATE_CHANGED";
    public static final int RC_SEARCH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateContent(binding.getRoot());
        setToolBarContent("", true, false);
        initToolbarCustomView();
        showProgressDialog(getString(R.string.please_Wait), false, true);
        setActionBar();
        registerBroadcastReceiver();
        binding.productsSwipeContainer.setOnRefreshListener(this);

        new RegisterGCMAsyncTask(getApplicationContext(), apiClient).execute();
    }

    private void registerBroadcastReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(HomeActivity.ORDERS_STATE_CHANGED);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    productsAdapter.notifyDataSetChanged();
                }
            };
            registerReceiver(broadcastReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolbarCustomView() {
        LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        customToolbarLayout = (RelativeLayout) inflater.inflate(R.layout.layout_custom_home_toolbar, null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(customToolbarLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void setActionBar() {
        setToolBarContent("", false, false);
        LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View spinnerHolder = inflater.inflate(R.layout.actionbar_spinner, null);
        spinner = (Spinner) spinnerHolder.findViewById(R.id.spinner);
        spinner.setAdapter(spinAdapter);
        initCategoriesItems(spinner, R.color.white);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(IntroActivity.CATEGORY_POSITION)) {
            selectedSpinnerPosition = getIntent().getExtras().getInt(IntroActivity.CATEGORY_POSITION);
            spinner.setSelection(selectedSpinnerPosition);
        } else
            selectedSpinnerPosition = 0;
        getSupportActionBar().setCustomView(spinnerHolder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() != null) {
            String title = item.getTitle().toString();
            if (title.equals(getString(R.string.sign_out))) {
                signout();//ToDo: fix signout user flag
                return true;
            } else if (title.equals(getString(R.string.order_now))) {
                navigateActivity(new Intent(this, CartOrdersActivity.class));
            } else if (title.equals(getString(R.string.notifications)))
                navigateActivity(new Intent(this, TodayNotificationsActivity.class));
        }

        if (item.getItemId() == R.id.action_search) {
            View searchMenuView = baseBinding.toolbar.toolbar.findViewById(R.id.action_search);
            int[] loc = new int[2];
            searchMenuView.getLocationOnScreen(loc);
            Intent searchIntent;
            int selectedSpinnerPosition = spinner.getSelectedItemPosition();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                searchMenuView.getLocationOnScreen(loc);
                searchIntent = SearchActivity.createStartIntent(this, loc[0], loc[0] +
                        (searchMenuView.getWidth() / 2), true);
                searchIntent.putExtra(IntroActivity.CATEGORY_POSITION, selectedSpinnerPosition);
                startActivityForResult(searchIntent, RC_SEARCH);
            } else {
                searchIntent = new Intent(this, SearchActivity.class);
                searchIntent.putExtra(IntroActivity.CATEGORY_POSITION, selectedSpinnerPosition);
                startActivityForResultWithTransition(this, searchIntent, RC_SEARCH);
            }
            searchMenuView.setAlpha(0f);
        }

        return true;
    }

    @Override
    protected void onCategorySelected(int position) {
        super.onCategorySelected(position);
        page = 1;
        binding.productsRecyclerView.getLayoutManager().scrollToPosition(0);
        binding.productsRecyclerView.scrollToPosition(0);
        requestProducts(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SEARCH) {
            // reset the search icon which we hid
            View searchMenuView = baseBinding.toolbar.toolbar.findViewById(R.id.action_search);
            if (searchMenuView != null) {
                searchMenuView.setAlpha(1f);
            }
        }
    }
}

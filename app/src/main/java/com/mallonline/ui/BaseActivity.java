package com.mallonline.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallonline.R;
import com.mallonline.api.ApiClient;
import com.mallonline.api.ApiClientImp;
import com.mallonline.api.ClientCallback;
import com.mallonline.async.UnRegisterGCM;
import com.mallonline.data.SharedPref;
import com.mallonline.databinding.ActivityBaseBinding;
import com.mallonline.databinding.BaseToolbarBinding;
import com.mallonline.models.ServerResponse;
import com.mallonline.util.UtilityMethods;

import java.lang.reflect.Field;

/**
 * Created by Dina on 05/09/2016.
 */

public class BaseActivity extends AppCompatActivity {

    protected ActivityBaseBinding baseBinding;
    protected ApiClient apiClient;
    protected ProgressDialog progressDialog;
    protected SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_base, null, false);
        setContentView(baseBinding.getRoot());
        apiClient = ApiClientImp.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_color_dark));
        }

        applyTransition(this);
        initToolbar();
        initOrientation();
    }

    private void initOrientation() {
        try {
            boolean tablet = getResources().getBoolean(R.bool.tablet);
            //Todo: check why tablet check fails in landscape mode
//            if (tablet)
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected Activity applyTransition(Activity activity) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = new Slide();
            ((Slide) transition).setSlideEdge(Gravity.RIGHT);
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            transition.excludeTarget(android.R.id.navigationBarBackground, true);
            activity.getWindow().setEnterTransition(transition);
//            activity.getWindow().setExitTransition(transition);
        }
        return activity;
    }

    private void initToolbar() {
        if (baseBinding.toolbar != null) {
            setSupportActionBar(((BaseToolbarBinding) baseBinding.toolbar).toolbar);
        }
    }

    protected void inflateContent(View view) {
        RelativeLayout contentContainerLayout = baseBinding.baseCustomView;
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        contentContainerLayout.removeAllViews();
        contentContainerLayout.addView(view);
    }

    protected void showProgressDialog(String dialogMessage, boolean isCancelable, boolean isIndeteminate) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(dialogMessage);
            progressDialog.setCancelable(isCancelable);
            progressDialog.setIndeterminate(isIndeteminate);
            progressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    protected void setError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setError(error);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivityWithTransition(Activity activity, Intent intent) {
        ActivityOptionsCompat options = null;
        try {
            options = getOptionsCompat(activity);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private ActivityOptionsCompat getOptionsCompat(Activity activity) {
        ActivityOptionsCompat options = null;

        try {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        applyTransition(activity);
        return options;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void signout() {
        showProgressDialog(getString(R.string.please_Wait), false, true);
        apiClient.deleteDeviceWithRegId(SharedPref.LoadString(this, SharedPref.PROPERTY_REG_ID), new ClientCallback() {
            @Override
            public void onServerResultSuccess(Object o) {
                if (!isFinishing())
                    dismissProgressDialog();
                SharedPref.deleteShared(BaseActivity.this);
                Intent i = new Intent(BaseActivity.this, LoginActivity.class);
                startActivityWithTransition(BaseActivity.this, i);
                finishActivity(BaseActivity.this);
                new UnRegisterGCM(BaseActivity.this).execute();
            }

            @Override
            public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
                showFailureMessage(serverResponse, statusCode);
            }
        });
    }

    protected void setToolBarContent(String title, boolean showTitle, boolean showHome) {
        if (!"".equals(title)) {
            getSupportActionBar().setTitle(title);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void finishActivity(Activity activity) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAfterTransition();
        } else {
            activity.finish();
        }
    }

    protected void navigateActivity(Intent intent) {
        startActivityWithTransition(this, intent);
    }

    protected void isShowHome(boolean isShowHome) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShowHome);
        getSupportActionBar().setDisplayShowHomeEnabled(isShowHome);
    }

    protected void showFailureMessage(ServerResponse serverResponse, int statusCode) {
        showFailureMessage(serverResponse, statusCode, baseBinding.getRoot());
    }

    protected void showFailureMessage(ServerResponse serverResponse, int statusCode, View snackbarView) {
        String errorMessage = getString(R.string.something_wrong);
        if (serverResponse != null && serverResponse.getErrorMessageStr() != null && !"".equals((serverResponse.getErrorMessageStr()).trim()))
            errorMessage = serverResponse.getErrorMessageStr();

        if (!isFinishing()) {
            UtilityMethods.showSnackbar(errorMessage, snackbarView, this);
            dismissProgressDialog();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivityForResultWithTransition(Activity activity, Intent intent, int requestCode) {
        ActivityOptionsCompat options = null;
        try {
            options = getOptionsCompat(activity);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle());//startActivity(activity, intent, options.toBundle());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    protected boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    protected void initSearchActions(final SearchView searchView) {
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        this.searchView = searchView;
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                menu.findItem()
                onSearchIconClicked();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty())
                    onSearchQueryTextSubmit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearchQueryTextChanged(newText);
                return false;
            }
        });

        View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery() != null && !TextUtils.isEmpty(searchView.getQuery().toString())) {
                    searchView.setQuery("", true);
                    onClearSearchSelected();
                } else
                    searchView.setIconified(true);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onSearchViewClosed();
                return false;
            }
        });

        AutoCompleteTextView searchTextView = null;
        try {
            searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_view_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addSearchOption(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        initSearchActions((SearchView) menu.findItem(R.id.action_search).getActionView());
    }

    protected void onSearchIconClicked() {

    }

    protected void onClearSearchSelected() {
    }

    protected void onSearchQueryTextSubmit(String query) {

    }

    protected void onSearchQueryTextChanged(String queryStr) {

    }

    protected void onSearchViewClosed() {

    }
}

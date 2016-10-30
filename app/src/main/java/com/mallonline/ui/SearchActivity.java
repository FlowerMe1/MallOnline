package com.mallonline.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.mallonline.R;
import com.mallonline.databinding.ActivitySearchBinding;
import com.mallonline.models.Category;
import com.mallonline.models.ServerResponse;
import com.mallonline.util.AnimUtils;
import com.mallonline.util.ImeUtils;
import com.mallonline.util.ViewUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;

public class SearchActivity extends BaseProductsActivity {


    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
    protected ActivitySearchBinding binding;
    private Transition auto;
    private int searchBackDistanceX;
    private int searchIconCenterX;
    private String productsSearchStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.search_theme);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_search, null, false);
        productsSwipeContainer = binding.layoutProducts.productsSwipeContainer;
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSearchView();
        initRecyclerView(binding.layoutProducts.productsRecyclerView);
        if (isLollipop()) {
            auto = TransitionInflater.from(this).inflateTransition(R.transition.auto);
            initAnimations();
        } else
            binding.layoutSpinnerContainer.setVisibility(View.VISIBLE);

        if (!isLollipop()) {
            binding.searchback.setImageResource(R.drawable.back_icon);
            binding.searchback.setPadding((int) getResources().getDimension(R.dimen.back_padding), (int) getResources().getDimension(R.dimen.back_padding), (int) getResources().getDimension(R.dimen.back_padding), (int) getResources().getDimension(R.dimen.back_padding));
            binding.searchBackground.setAlpha(1);
            binding.searchView.setAlpha(1);
        }

        binding.layoutProducts.productsSwipeContainer.setEnabled(false);
        spinner = binding.spinner;
        initCategoriesItems(spinner, R.color.primary_txt_color);
        initSpinnerPosition();
    }

    private void initSpinnerPosition() {
        if (getIntent() != null && getIntent().hasExtra(IntroActivity.CATEGORY_POSITION)) {
            binding.spinner.setSelection(getIntent().getIntExtra(IntroActivity.CATEGORY_POSITION, 0));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAnimations() {

        // extract the search icon's location passed from the launching activity, minus 4dp to
        // compensate for different paddings in the views
        searchBackDistanceX = getIntent().getIntExtra(EXTRA_MENU_LEFT, 0) - (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        searchIconCenterX = getIntent().getIntExtra(EXTRA_MENU_CENTER_X, 0);

        // translate icon to match the launching screen then animate back into position
        binding.searchbackContainer.setTranslationX(searchBackDistanceX);
        binding.searchbackContainer.animate()
                .translationX(0f)
                .setDuration(650L)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this));
        // transform from search icon to back icon
        AnimatedVectorDrawable searchToBack = (AnimatedVectorDrawable) ContextCompat
                .getDrawable(this, R.drawable.avd_search_to_back);
        binding.searchback.setImageDrawable(searchToBack);
        searchToBack.start();
        // for some reason the animation doesn't always finish (leaving a part arrow!?) so after
        // the animation set a static drawable. Also animation callbacks weren't added until API23
        // so using post delayed :(
        // TODO fix properly!!
        binding.searchback.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.searchback.setImageDrawable(ContextCompat.getDrawable(SearchActivity.this,
                        R.drawable.ic_arrow_back_padded));
            }
        }, 600L);

        // fade in the other search chrome
        binding.searchBackground.animate()
                .alpha(1f)
                .setDuration(300L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(this));
        searchView.animate()
                .alpha(1f)
                .setStartDelay(400L)
                .setDuration(400L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(this))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchView.requestFocus();
                        ImeUtils.showIme(searchView);
                    }
                });

        animateScrim();
    }

    private void animateScrim() {
        // animate in a scrim over the content behind
        binding.scrim.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPreDraw() {
                binding.scrim.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimatorSet showScrim = new AnimatorSet();
                showScrim.playTogether(
                        ViewAnimationUtils.createCircularReveal(
                                binding.scrim,
                                searchIconCenterX,
                                binding.searchBackground.getBottom(),
                                0,
                                (float) Math.hypot(searchBackDistanceX, binding.scrim.getHeight()
                                        - binding.searchBackground.getBottom())),
                        ObjectAnimator.ofArgb(
                                binding.scrim,
                                ViewUtils.BACKGROUND_COLOR,
                                Color.TRANSPARENT,
                                ContextCompat.getColor(SearchActivity.this, R.color.scrim)));
                showScrim.setDuration(400L);
                showScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(SearchActivity
                        .this));

                showScrim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.layoutSpinnerContainer.setVisibility(View.VISIBLE);
                    }
                });

                showScrim.start();
                return false;
            }
        });
    }

    @Override
    protected void onSearchQueryTextSubmit(String queryStr) {
        this.productsSearchStr = queryStr;
        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        searchFor();
        super.onSearchQueryTextSubmit(queryStr);
        Category selectedCategory = categoryItems.get(spinner.getSelectedItemPosition());
        if (selectedCategory != null)
            apiClient.searchProduct(selectedCategory.getServerId(), queryStr, page, this);
    }

    @Override
    protected void onSearchQueryTextChanged(String queryStr) {
        super.onSearchQueryTextChanged(queryStr);
        handleSearchViewCloseBtnVisibility(queryStr);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRecyclerViewColsNum();
        binding.layoutProducts.productsRecyclerView.setLayoutManager(layoutManager);
        productsAdapter.notifyDataSetChanged();
    }


    private void handleSearchViewCloseBtnVisibility(String queryStr) {
        try {
            View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            if (queryStr == null || queryStr.isEmpty()) {
                closeButton.setVisibility(View.GONE);
                /*to prevent showing close icon when back is pressed*/
                ((ImageView) closeButton).setImageDrawable(null);
                closeButton.setEnabled(false);
                clearProducts();
                clearResults();
            } else {
                closeButton.setVisibility(View.VISIBLE);
                closeButton.setEnabled(true);
                ((ImageView) closeButton).setImageDrawable(ActivityCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_clear_material));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchFor() {
        clearResults();
        binding.resultsContainer.setVisibility(View.VISIBLE);
        setLoadingProgressBarVisibility(View.VISIBLE);
        binding.searchBackground.setVisibility(View.VISIBLE);
        ImeUtils.hideIme(searchView);
        searchView.clearFocus();
    }

    private void setLoadingProgressBarVisibility(int visibility) {
        binding.loadingProgressBar.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            binding.stubNoSearchResults.noResultsTextView.setText("");
        }
    }

    protected void clearProducts() {
        binding.layoutProducts.productsRecyclerView.scrollToPosition(0);
        productList.clear();
        productsAdapter.clearItems();
        productsAdapter.notifyDataSetChanged();
    }

    protected void clearResults() {
        binding.layoutProducts.getRoot().setVisibility(View.GONE);
        binding.loadingProgressBar.setVisibility(View.GONE);
        binding.stubNoSearchResults.getRoot().setVisibility(View.GONE);
    }

    private void setNoResultsVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            String message = String.format(getString(R
                    .string.no_search_results), searchView.getQuery().toString());
            SpannableStringBuilder ssb = new SpannableStringBuilder(message);
            ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.stubNoSearchResults.noResultsTextView.setText(ssb);
            binding.stubNoSearchResults.getRoot().setVisibility(View.VISIBLE);
            binding.loadingProgressBar.setVisibility(View.GONE);

            binding.stubNoSearchResults.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSearchQueryText();
                }
            });
        }
        binding.stubNoSearchResults.getRoot().setVisibility(visibility);
    }

    @Override
    protected void onClearSearchSelected() {
        super.onClearSearchSelected();
//        clearResults();
    }

    private void clearSearchQueryText() {
        searchView.setQuery("", false);
        searchView.requestFocus();
        ImeUtils.showIme(searchView);
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {
        super.onServerResultSuccess(jsonObject);
        if (page == 1) {
            binding.layoutProducts.getRoot().setVisibility(View.VISIBLE);
            binding.loadingProgressBar.setVisibility(View.GONE);
            if (productList == null || productList.size() == 0) {
                showNoResults();
            } else {
                setNoResultsVisibility(View.GONE);
                binding.layoutProducts.getRoot().setVisibility(View.VISIBLE);
            }
            if (isLollipop())
                TransitionManager.beginDelayedTransition(binding.container, auto);
        }
    }

    private void showNoResults() {
        setNoResultsVisibility(View.VISIBLE);
        binding.layoutProducts.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        showFailureMessage(serverResponse, statusCode, binding.getRoot());
        showNoResults();
    }


    private void initSearchView() {
        searchView = binding.searchView;
        searchView.setIconified(false);
        Field f = null;
        try {
            f = TextView.class.getDeclaredField("mCursorDrawableRes");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);

        try {
            f.set(searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text), R.drawable.search_cursor);
            View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            closeButton.setEnabled(false);
            closeButton.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(Html.fromHtml("<font color = #aaffffff>" + getResources().getString(R.string.search_hint) + "</font>"));


        try {
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchView.findViewById(id);
            textView.setTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        searchManager.getSearchablesInGlobalSearch();
        initSearchActions(searchView);
    }

    public static Intent createStartIntent(Context context, int menuIconLeft, int menuIconCenterX, boolean isHome) {
        Intent starter;
        starter = new Intent(context, SearchActivity.class);
//        else
//            starter = new Intent(context, SearchTournamentsGameShowsActivity.class);
        starter.putExtra(EXTRA_MENU_LEFT, menuIconLeft);
        starter.putExtra(EXTRA_MENU_CENTER_X, menuIconCenterX);
        return starter;
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    public void onBackButtonClicked(View view) {
        closeActivity();
    }

    protected void closeActivity() {
        if (isLollipop())
            dismiss();
        else
            finishActivity(this);
    }


    /*dismiss -- close activity animation*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void dismiss() {
//        if (dismissing) return;
//        dismissing = true;

        // translate the icon to match position in the launching activity
        binding.searchbackContainer.animate()
                .translationX(searchBackDistanceX)
                .setDuration(600L)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishAfterTransition();
                    }
                })
                .start();
        // transform from back icon to search icon
        AnimatedVectorDrawable backToSearch = (AnimatedVectorDrawable) ContextCompat
                .getDrawable(this, R.drawable.avd_back_to_search);
        binding.searchback.setImageDrawable(backToSearch);
        // clear the background else the touch ripple moves with the translation which looks bad
        binding.searchback.setBackground(null);
        backToSearch.start();
        // fade out the other search chrome
        searchView.animate()
                .alpha(1f)
                .setStartDelay(0L)
                .setDuration(120L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // prevent clicks while other anims are finishing
                        searchView.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
        binding.searchBackground.animate()
                .alpha(1f)
                .setStartDelay(300L)
                .setDuration(160L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this))
                .setListener(null)
                .start();
        if (binding.searchToolbar.getZ() != 0f) {
            binding.searchToolbar.animate()
                    .z(0f)
                    .setDuration(600L)
                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this))
                    .start();
        }

        // if we're showing search results, circular hide them
        if (binding.resultsContainer.getHeight() > 0) {
            Animator closeResults = ViewAnimationUtils.createCircularReveal(
                    binding.resultsContainer,
                    searchIconCenterX,
                    0,
                    (float) Math.hypot(searchIconCenterX, binding.resultsContainer.getHeight()),
                    0f);
            closeResults.setDuration(500L);
            closeResults.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(SearchActivity
                    .this));
            closeResults.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.resultsContainer.setVisibility(View.INVISIBLE);
                }
            });
            closeResults.start();
        }

        // fade out the scrim
        binding.scrim.animate()
                .alpha(0f)
                .setDuration(400L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this))
                .setListener(null)
                .start();
    }

    @Override
    public void onCartStateChanged() {
        super.onCartStateChanged();
        Intent intent = new Intent(HomeActivity.ORDERS_STATE_CHANGED);
        intent.setAction(HomeActivity.ORDERS_STATE_CHANGED);
        sendBroadcast(intent);
    }
}

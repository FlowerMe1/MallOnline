package com.mallonline.ui;

/**
 * Created by Dina on 01/10/2016.
 */

public class SearchHomeProductsActivity extends SearchActivity {
//    protected boolean isReloadFeedsWithNavigation = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.snackbarLayout = binding.getRoot();
//    }
//
//    @Override
//    protected void loadPaginationFeeds() {
//        apiClient.getFeeds(this, pagination.getNextPage(), LOADED_ITEMS_COUNT, feedsSearchStr);
//    }
//
//    @Override
//    protected void setRecyclerViewAdapter() {
//        adapter = new FeedsAdapter(this, feedsList, this, this, true);
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    protected void unsubscribeServerSuccess() {
//        isReloadFeedsWithNavigation = true;
//    }
//
//    @Override
//    protected void onSearchQueryTextSubmit(String queryStr) {
//        super.onSearchQueryTextSubmit(queryStr);
//        refreshFeeds(true);
//    }
//
//    private void refreshFeeds(boolean isSelected) {
//        recyclerView.scrollToPosition(0);
//        apiClient.getFeeds(this, FIRST_PAGE, LOADED_ITEMS_COUNT, feedsSearchStr);
//        feedsSwipeContainer.setEnabled(false);
//        if (isSelected) {
//            refreshFeedsSelected();
//        }
//    }
//
//    @Override
//    protected void clearResults(){
//        feedsList.clear();
//        adapter.clearItems();
//        super.clearResults();
//    }
//
//    @Override
//    protected void onScrollTopSelected() {
//
//    }
//
//    @Override
//    protected void closeActivity() {
//        Intent intent = new Intent();
//        intent.putExtra(HomeActivity.RELOAD_FEEDS, isSubscriptionStateChanged);
//        setResult(RESULT_OK, intent);
//        super.closeActivity();
//    }
}

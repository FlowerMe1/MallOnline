package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.mallonline.R;
import com.mallonline.adapter.ProductImagesAdapter;
import com.mallonline.databinding.ActivityProductDetailsBinding;
import com.mallonline.manager.OrdersManager;
import com.mallonline.manager.UsersManager;
import com.mallonline.models.Product;

public class ProductDetailsActivity extends BaseActivity {

    public final static String PRODUCT_DETAILS = "PRODUCT_DETAILS";

    private ActivityProductDetailsBinding binding;
    private OrdersManager ordersManager;
    private UsersManager usersManager;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersManager = OrdersManager.getInstance();
        usersManager = UsersManager.getInstance();
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_product_details, null, false);
        setToolBarContent(getString(R.string.product_details), true, true);
        inflateContent(binding.getRoot());
        initProductFields();
        initAddRemoveCartButton();
    }

    private void initAddRemoveCartButton() {
        if (ordersManager.isAddedToCart(usersManager.getCurrentUser().getServerId(), product.getServerId()))
            binding.addRemoveFromCart.setText(getString(R.string.remove_from_cart));
        else
            binding.addRemoveFromCart.setText(getString(R.string.add_to_cart));
    }

    public void onAddRemoveCartClicked(View view) {
        if (ordersManager.isAddedToCart(usersManager.getCurrentUser().getServerId(), product.getServerId())) {
            OrdersManager.getInstance().removeFromCart(usersManager.getCurrentUser().getServerId(), product.getServerId());
            binding.addRemoveFromCart.setText(getString(R.string.add_to_cart));
        } else {
            OrdersManager.getInstance().addToCard(usersManager.getCurrentUser().getServerId(), product.getServerId());
            binding.addRemoveFromCart.setText(getString(R.string.remove_from_cart));
        }
         /*notify home activity*/
        Intent intent = new Intent(HomeActivity.ORDERS_STATE_CHANGED);
        intent.setAction(HomeActivity.ORDERS_STATE_CHANGED);
        sendBroadcast(intent);
    }

    private void initProductFields() {
        product = getIntent().getParcelableExtra(PRODUCT_DETAILS);
        if (product != null) {
            binding.productTitle.setText(product.getTitle());
            binding.productDescription.setText(product.getDescription());
            binding.priceTextView.setText(String.valueOf(product.getPrice()));
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.productsImagesRecyclerView;

        if (product.getImages() != null && product.getImages().size() > 0) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(product.getImages(), this);
            recyclerView.setAdapter(productImagesAdapter);
            recyclerView.setItemAnimator(null);
        } else
            recyclerView.setVisibility(View.GONE);
    }
}

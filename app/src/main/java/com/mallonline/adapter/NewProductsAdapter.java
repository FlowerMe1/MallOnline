package com.mallonline.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.mallonline.R;
import com.mallonline.databinding.NewProductCellBinding;
import com.mallonline.listeners.ItemSelectedListener;
import com.mallonline.listeners.OrderCartStateListener;
import com.mallonline.manager.OrdersManager;
import com.mallonline.manager.UsersManager;
import com.mallonline.models.Product;
import com.mallonline.util.UtilityMethods;

import java.util.List;


/**
 * Created by Dina on 06/09/2016.
 */

public class NewProductsAdapter extends BaseAdapter {

    private AQuery aQuery;
    private Context context;
    private UsersManager usersManager;
    private OrdersManager ordersManager;
    private ItemSelectedListener productSelectedListener;
    private boolean isFilterNewArrivals = false;
    private OrderCartStateListener cartStateListener;

    public NewProductsAdapter(List<Product> products, Context context, ItemSelectedListener productSelectedListener, boolean isFilterNewArrivals, OrderCartStateListener cartStateListener) {
        super(products);
        this.context = context;
        this.productSelectedListener = productSelectedListener;
        aQuery = new AQuery(context);
        usersManager = UsersManager.getInstance();
        ordersManager = OrdersManager.getInstance();
        this.isFilterNewArrivals = isFilterNewArrivals;
        this.cartStateListener = cartStateListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return super.onCreateViewHolder(parent, viewType);
        }

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        final NewProductCellBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.new_product_cell, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        int type = getItemViewType(position);

        if (type == TYPE_EMPTY)
            return;

        final ViewHolder holder = (ViewHolder) viewHolder;
        final Product product = (Product) items.get(position);
        holder.binding.productTitleTextView.setText(product.getTitle());
        holder.binding.productDescriptionTextView.setText(product.getDescription());
        final boolean isAddedToCart = ordersManager.isAddedToCart(usersManager.getCurrentUser().getServerId(), product.getServerId());

        if (isAddedToCart)
            holder.binding.addToCart.setText(context.getString(R.string.remove_from_cart));
        else
            holder.binding.addToCart.setText(context.getString(R.string.add_to_cart));

        holder.binding.addToCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAddedToCart = ordersManager.isAddedToCart(usersManager.getCurrentUser().getServerId(), product.getServerId());
                if (isAddedToCart) {
                    OrdersManager.getInstance().removeFromCart(usersManager.getCurrentUser().getServerId(), product.getServerId());
                    holder.binding.addToCart.setText(context.getString(R.string.add_to_cart));
                } else {
                    OrdersManager.getInstance().addToCard(usersManager.getCurrentUser().getServerId(), product.getServerId());
                    holder.binding.addToCart.setText(context.getString(R.string.remove_from_cart));
                }
                if (cartStateListener != null)
                    cartStateListener.onCartStateChanged();
            }
        });

        holder.binding.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productSelectedListener != null)
                    productSelectedListener.onItemSelected(product);
            }
        });

        if (product.getImages() != null && product.getImages().size() > 0) {
            showImage(holder);
            setAQueryImg(product.getImages().get(0), holder);
        } else if (product.getImage() != null && !"".equals(product.getImage().trim())) {
            showImage(holder);
            setAQueryImg(product.getImage(), holder);
        } else
            hideImage(holder);

        if (product.isNewArrival() && !isFilterNewArrivals)
            holder.binding.newTextView.setVisibility(View.VISIBLE);
        else
            holder.binding.newTextView.setVisibility(View.GONE);

        holder.binding.priceTextView.setText(String.valueOf(product.getPrice()));
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.binding.productCardView.getLayoutParams();
        try {
            if (position % 2 == 0) {
                params.setMargins((int) context.getResources().getDimension(R.dimen.recycler_view_margin), (int) context.getResources().getDimension(R.dimen.recycler_view_margin), (int) context.getResources().getDimension(R.dimen.recycler_view_margin), 0);
            } else {
                params.setMargins(0, (int) context.getResources().getDimension(R.dimen.recycler_view_margin), (int) context.getResources().getDimension(R.dimen.recycler_view_margin), 0);
            }
            holder.binding.productCardView.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final NewProductCellBinding binding;

        ViewHolder(final NewProductCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    private void setAQueryImg(final String imgUrl, final ViewHolder holder) {
        aQuery.id(holder.binding.productImage).
                progress(holder.binding.imageProgress).image(imgUrl, false, false, 0, 0, new BitmapAjaxCallback() {
            @Override
            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                if (bm != null) {
                    int requiredImageHeight = (int) context.getResources().getDimension(R.dimen.product_img_height);
                    int bitmapWidth = UtilityMethods.getBitmapWidth(bm, requiredImageHeight);
                    Bitmap feedImageBitmap = UtilityMethods.scaleBitmap(bm, bitmapWidth, requiredImageHeight);
                    iv.setImageBitmap(feedImageBitmap);
                    showImage(holder);
                } else
                    hideImage(holder);
            }
        });
    }

    private void showImage(ViewHolder holder) {
        ((LinearLayout.LayoutParams) holder.binding.layoutImageContainer.getLayoutParams()).height = (int) context.getResources().getDimension(R.dimen.product_img_height);
        holder.binding.productImage.setVisibility(View.VISIBLE);
        holder.binding.imageProgress.setVisibility(View.VISIBLE);
        holder.binding.imageProgress.setVisibility(View.GONE);
    }

    private void hideImage(ViewHolder holder) {
        ((LinearLayout.LayoutParams) holder.binding.layoutImageContainer.getLayoutParams()).height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.binding.productImage.setVisibility(View.GONE);
        holder.binding.imageProgress.setVisibility(View.GONE);
    }

    public void setFilterNewArrivals(boolean isFilterNewArrivals) {
        this.isFilterNewArrivals = isFilterNewArrivals;
    }

}

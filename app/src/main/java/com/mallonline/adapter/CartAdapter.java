package com.mallonline.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallonline.R;
import com.mallonline.databinding.CartCellBinding;
import com.mallonline.databinding.CartOrderCellBinding;
import com.mallonline.databinding.NewProductCellBinding;
import com.mallonline.listeners.ItemRemovedListener;
import com.mallonline.manager.OrdersManager;
import com.mallonline.manager.UsersManager;
import com.mallonline.models.Product;

import java.util.List;

/**
 * Created by Dina on 11/09/2016.
 */

public class CartAdapter extends BaseAdapter {

    private UsersManager usersManager;
    private OrdersManager ordersManager;
    private ItemRemovedListener itemRemovedListener;

    public CartAdapter(List<Product> products, ItemRemovedListener itemRemovedListener) {
        super(products);
        this.itemRemovedListener = itemRemovedListener;
        usersManager = UsersManager.getInstance();
        ordersManager = OrdersManager.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return super.onCreateViewHolder(parent, viewType);
        }

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        final CartOrderCellBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.cart_order_cell, parent, false);
        return new CartAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        int type = getItemViewType(position);

        if (type == TYPE_EMPTY)
            return;

        final CartAdapter.ViewHolder holder = (CartAdapter.ViewHolder) viewHolder;
        final Product product = (Product) items.get(position);
        holder.binding.productTitleTextView.setText(product.getTitle());
        holder.binding.productDescriptionTextView.setText(product.getDescription());
        holder.binding.priceTextView.setText(String.valueOf(product.getPrice()));
        holder.binding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersManager.removeFromCart(usersManager.getCurrentUser().getServerId(), ((Product) items.get(position)).getServerId());
                removeItem(position);
                if (itemRemovedListener != null)
                    itemRemovedListener.notifyItemRemoved();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final CartOrderCellBinding binding;

        ViewHolder(final CartOrderCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

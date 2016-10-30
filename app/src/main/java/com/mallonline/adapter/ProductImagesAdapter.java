package com.mallonline.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.mallonline.R;
import com.mallonline.databinding.NewProductCellBinding;
import com.mallonline.databinding.ProductImageCellBinding;
import com.mallonline.util.UtilityMethods;

import java.util.List;

/**
 * Created by Dina on 15/10/2016.
 */

public class ProductImagesAdapter extends BaseAdapter {

    private AQuery aQuery;
    private Context context;

    public ProductImagesAdapter(List<String> imageList, Context context) {
        super(imageList);
        this.aQuery = new AQuery(context);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return super.onCreateViewHolder(parent, viewType);
        }

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        final ProductImageCellBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.product_image_cell, parent, false);
        return new ProductImagesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        int type = getItemViewType(position);

        if (type == TYPE_EMPTY)
            return;

        final ProductImagesAdapter.ViewHolder holder = (ProductImagesAdapter.ViewHolder) viewHolder;
        String imageUrl = (String) items.get(position);

        if (imageUrl != null) {
            holder.binding.imageProgress.setVisibility(View.VISIBLE);
            holder.binding.productImage.setVisibility(View.VISIBLE);
            setAQueryImg(imageUrl, holder);
        } else
            holder.binding.layoutImage.setVisibility(View.GONE);
    }

    private void setAQueryImg(final String imgUrl, final ViewHolder holder) {
        aQuery.id(holder.binding.productImage).
                progress(holder.binding.imageProgress).image(imgUrl, false, false, 0, 0, new BitmapAjaxCallback() {
            @Override
            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                if (bm != null) {
                    int requiredImageHeight = (int) context.getResources().getDimension(R.dimen.product_details_img_height);
                    int bitmapWidth = UtilityMethods.getBitmapWidth(bm, requiredImageHeight);
                    Bitmap feedImageBitmap = UtilityMethods.scaleBitmap(bm, bitmapWidth, requiredImageHeight);
                    iv.setImageBitmap(feedImageBitmap);
                    holder.binding.imageProgress.setVisibility(View.GONE);
                }else
                    holder.binding.layoutImage.setVisibility(View.GONE);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ProductImageCellBinding binding;

        ViewHolder(final ProductImageCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

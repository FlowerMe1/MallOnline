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
import com.mallonline.databinding.NotificationCellBinding;
import com.mallonline.listeners.ItemSelectedListener;
import com.mallonline.models.Notification;
import com.mallonline.util.UtilityMethods;

import java.util.List;

/**
 * Created by Dina on 19/09/2016.
 */

public class NotificationAdapter extends BaseAdapter {

    private ItemSelectedListener listener;
    private AQuery aQuery;
    private Context context;

    public NotificationAdapter(List<Notification> notifications, ItemSelectedListener listener, Context context) {
        super(notifications);
        this.listener = listener;
        this.context = context;
        this.aQuery = new AQuery(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return super.onCreateViewHolder(parent, viewType);
        }

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        final NotificationCellBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.notification_cell, parent, false);
        return new NotificationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        int type = getItemViewType(position);

        if (type == TYPE_EMPTY)
            return;

        final NotificationAdapter.ViewHolder holder = (NotificationAdapter.ViewHolder) viewHolder;
        com.mallonline.models.Notification notification = (com.mallonline.models.Notification) items.get(position);
        holder.binding.notificationTitleTextView.setText(notification.getTitle());
        holder.binding.notificationMessageTextView.setText(notification.getMessage());
//        setAQueryImg(notification.getPictureUrl(), holder);
        if (notification.getPictureUrl() != null && !"".equals(notification.getPictureUrl().trim())) {
            holder.binding.notificationImageView.setImageBitmap(null);
            holder.binding.layoutNotificationContainer.setVisibility(View.VISIBLE);
            holder.binding.imageProgress.setVisibility(View.VISIBLE);
            aQuery.id(holder.binding.notificationImageView).progress(holder.binding.imageProgress).image(notification.getPictureUrl(), false, false);
        } else {
            holder.binding.imageProgress.setVisibility(View.VISIBLE);
            holder.binding.layoutNotificationContainer.setVisibility(View.GONE);
            holder.binding.notificationImageView.setImageBitmap(null);
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(items.get(position));
            }
        });
    }

    private void setAQueryImg(final String imgUrl, final NotificationAdapter.ViewHolder holder) {
        if (imgUrl != null && !"".equals(imgUrl.trim())) {
            showImage(holder);
            aQuery.id(holder.binding.notificationImageView).
                    progress(holder.binding.imageProgress).image(imgUrl, false, false, 0, 0, new BitmapAjaxCallback() {
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    if (bm != null) {
                        float requiredImageSize = context.getResources().getDimension(R.dimen.notification_image_size);
                        Bitmap notificationImageBitmap = UtilityMethods.scaleBitmap(bm, requiredImageSize, requiredImageSize);
                        iv.setImageBitmap(notificationImageBitmap);
                    }
//                    else
//                        iv.setImageResource(R.drawable.logo);
                }
            });
        } else
            hideImage(holder);
    }

    private void showImage(NotificationAdapter.ViewHolder holder) {
        holder.binding.notificationImageView.setVisibility(View.VISIBLE);
        holder.binding.imageProgress.setVisibility(View.GONE);
        holder.binding.layoutNotificationContainer.setVisibility(View.VISIBLE);
    }

    private void hideImage(NotificationAdapter.ViewHolder holder) {
        holder.binding.layoutNotificationContainer.setVisibility(View.GONE);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final NotificationCellBinding binding;

        ViewHolder(final NotificationCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

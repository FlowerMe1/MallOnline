package com.mallonline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.mallonline.R;
import com.mallonline.databinding.DialogNotificationBinding;
import com.mallonline.models.Notification;
import com.mallonline.util.UtilityMethods;

/**
 * Created by Dina on 20/09/2016.
 */

public class NotificationDetailsDialog extends Dialog implements View.OnClickListener {

    private DialogNotificationBinding binding;
    private AQuery aQuery;
    private Context context;

    public NotificationDetailsDialog(Context context, Notification notification) {
        super(context, R.style.dialog_theme);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_notification, null, false);
        binding.setNotification(notification);
        setContentView(binding.getRoot());
        setTitle(notification.getTitle());
        getWindow().setWindowAnimations(R.style.dialog_animation_fade);
        getWindow().setGravity(Gravity.CENTER);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        this.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        binding.buttonCancel.setOnClickListener(this);
        this.aQuery = new AQuery(context);
        this.context = context;
        setAQueryImg(notification.getPictureUrl());
    }

    private void setAQueryImg(final String imgUrl) {
        if (imgUrl != null && !"".equals(imgUrl.trim())) {
            aQuery.id(binding.notificationImageView).
                    progress(binding.imageProgress).image(imgUrl, false, false, 0, 0, new BitmapAjaxCallback() {
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    if (bm != null) {
                        int requiredImageHeight = (int) context.getResources().getDimension(R.dimen.product_img_height);
                        int bitmapWidth = UtilityMethods.getBitmapWidth(bm, requiredImageHeight);
                        Bitmap feedImageBitmap = UtilityMethods.scaleBitmap(bm, bitmapWidth, requiredImageHeight);
                        iv.setImageBitmap(feedImageBitmap);
                        showImage();
                    } else
                        hideImage();
                }
            });
        } else
            hideImage();
    }

    private void showImage() {
        binding.imageLayout.setVisibility(View.VISIBLE);
        binding.imageProgress.setVisibility(View.GONE);
        binding.notificationImageView.setVisibility(View.VISIBLE);
    }

    private void hideImage() {
        binding.imageLayout.setVisibility(View.GONE);
        binding.imageProgress.setVisibility(View.GONE);
        binding.notificationImageView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}

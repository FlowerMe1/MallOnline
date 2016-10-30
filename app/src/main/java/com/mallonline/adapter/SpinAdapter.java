package com.mallonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallonline.R;
import com.mallonline.models.Category;

/**
 * Created by Kimo on 07/09/2016.
 */

public class SpinAdapter extends ArrayAdapter<Category> {
    private Context context;
    private Category[] values;
    private int textColor;

    public SpinAdapter(Context context, int textViewResourceId,
                       Category[] values, int textColor) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.textColor = textColor;
    }

    public int getCount() {
        return values.length;
    }

    public Category getItem(int position) {
        return values[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout spinnerParentLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.spinner_item, null, false);
        TextView itemTextView = (TextView) spinnerParentLayout.findViewById(R.id.category_text_view);
        itemTextView.setTextColor(context.getResources().getColor(textColor));
        itemTextView.setText(values[position].getTitle());
        return spinnerParentLayout;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        LinearLayout spinnerParentLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.spinner_item, null, false);
        TextView itemTextView = (TextView) spinnerParentLayout.findViewById(R.id.category_text_view);
        itemTextView.setTextColor(context.getResources().getColor(R.color.secondary_txt_color));
        itemTextView.setText(values[position].getTitle());

        try {
            String imageTitle = values[position].getTitle().toLowerCase();
            if (imageTitle.toLowerCase().contains("computers"))
                imageTitle = "computers";
            else if (imageTitle.toLowerCase().contains("kitchen"))
                imageTitle = "kitchen";
            else if (imageTitle.toLowerCase().contains("shoes"))
                imageTitle = "shoes";
            else if (imageTitle.toLowerCase().contains("watches"))
                imageTitle = "watches";
            else if (imageTitle.toLowerCase().contains("sport"))
                imageTitle = "sports";
            else if (imageTitle.toLowerCase().contains("toys"))
                imageTitle = "toys";
            else if (imageTitle.toLowerCase().contains("home"))
                imageTitle = "home";
            else if (imageTitle.toLowerCase().contains("health"))
                imageTitle = "health";
            else if (imageTitle.toLowerCase().contains("new"))
                imageTitle = "newarrivals";
            int id = context.getResources().getIdentifier(imageTitle, "drawable", context.getPackageName());
            ((ImageView) spinnerParentLayout.findViewById(R.id.category_image_view)).setImageResource(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spinnerParentLayout;
    }

    public void setItems(Category[] categories) {
        this.values = categories;
        notifyDataSetChanged();
    }
}

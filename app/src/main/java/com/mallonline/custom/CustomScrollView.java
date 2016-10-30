package com.mallonline.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.mallonline.R;

/**
 * Created by Dina on 05/10/2016.
 */

public class CustomScrollView extends ScrollView {

    private Context context;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 300;
        if (context != null)
            maxHeight = (int) context.getResources().getDimension(R.dimen.scrollview_maxheight);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

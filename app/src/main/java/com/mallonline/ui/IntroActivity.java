package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mallonline.R;
import com.mallonline.databinding.ActivityIntroBinding;

public class IntroActivity extends CategoriesBaseActivity {

    private ActivityIntroBinding binding;
    public final static String CATEGORY_POSITION = "CATEGORY_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_intro, null, false);
        inflateContent(binding.getRoot());
        setToolBarContent(getString(R.string.intro_title), true, false);
        initCategoriesItems(binding.spinner, R.color.primary_txt_color);
    }

    public void onCategorySelected(View view) {
        if (categoryItems != null && categoryItems.size() > 0) {
            Intent homeIntent = new Intent(IntroActivity.this, HomeActivity.class);
            homeIntent.putExtra(CATEGORY_POSITION, binding.spinner.getSelectedItemPosition());
            startActivityWithTransition(this, homeIntent);
            finishActivity(this);
        }
    }
}

package com.mallonline.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mallonline.R;
import com.mallonline.adapter.SpinAdapter;
import com.mallonline.api.ClientCallback;
import com.mallonline.async.SaveCategoriesAsyncTask;
import com.mallonline.manager.CategoriesManager;
import com.mallonline.models.Category;
import com.mallonline.models.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dina on 30/09/2016.
 */

public class CategoriesBaseActivity extends BaseActivity {

    protected List<Category> categoryItems;
    protected SpinAdapter spinAdapter;
    protected Spinner spinner;
    protected CategoriesManager categoriesManager;
    protected Gson gson;
    private int textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesManager = CategoriesManager.getInstance(this);
        gson = new Gson();
    }

    protected void initCategoriesItems(Spinner spinner, int textColor) {
        this.spinner = spinner;
        this.textColor = textColor;
        List<Category> categories = new ArrayList<>();
        if (categoriesManager.getAllCategories().size() == 0) {
            apiClient.getCategories(new ClientCallback<JSONObject>() {
                @Override
                public void onServerResultSuccess(JSONObject jsonObject) {

                    Type listType = new TypeToken<List<Category>>() {
                    }.getType();
                    List<Category> categoryList = null;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("categories");
                        categoryList = gson.fromJson(String.valueOf(jsonArray), listType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    refreshCategoriesList(categoryList);
                    new SaveCategoriesAsyncTask(CategoriesManager.getInstance(CategoriesBaseActivity.this), categoryList).execute();
                }

                @Override
                public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {

                }
            });
        } else
            categories = categoriesManager.getAllCategories();
        initCategoriesSpinner(categories);
    }

    private void initCategoriesSpinner(final List<Category> categoryList) {
        categoryItems = new ArrayList<>();
        categoryItems.clear();
        Category category = new Category();
        category.setTitle(getString(R.string.new_arrivals));
        categoryItems.add(category);
        categoryItems.addAll(categoryList);
        spinAdapter = new SpinAdapter(this, R.layout.spinner_item, categoryItems.toArray(new Category[categoryItems.size()]), textColor);
        spinner.setAdapter(spinAdapter);
//        spinner.setSelection(selectedSpinnerPosition, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                onCategorySelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void refreshCategoriesList(List<Category> categoryList) {
        if (categoryItems == null)
            categoryItems = new ArrayList<>();
        this.categoryItems.addAll(categoryList);
        spinAdapter.setItems(categoryItems.toArray(new Category[categoryList.size()]));
    }

    protected void onCategorySelected(int position) {

    }

}

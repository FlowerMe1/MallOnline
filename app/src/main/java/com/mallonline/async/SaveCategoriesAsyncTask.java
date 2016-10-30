package com.mallonline.async;

import android.os.AsyncTask;

import com.mallonline.manager.CategoriesManager;
import com.mallonline.models.Category;

import java.util.List;

/**
 * Created by Dina on 07/09/2016.
 */

public class SaveCategoriesAsyncTask extends AsyncTask {

    private CategoriesManager categoriesManager;
    private List<Category> categoryList;

    public SaveCategoriesAsyncTask(CategoriesManager categoriesManager, List<Category> categoryList){
        this.categoriesManager = categoriesManager;
        this.categoryList = categoryList;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        categoriesManager.saveCategories(categoryList);
        return null;
    }
}

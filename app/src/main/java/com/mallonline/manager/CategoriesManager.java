package com.mallonline.manager;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.mallonline.models.Category;

import java.util.List;

/**
 * Created by Dina on 07/09/2016.
 */

public class CategoriesManager {

    public final static String CATEGORY_SERVER_ID = "server_id";

    private static CategoriesManager instance;
    private Context context;

    public static CategoriesManager getInstance(Context context) {
        if (instance == null)
            instance = new CategoriesManager(context);
        return instance;
    }

    private CategoriesManager(Context context) {
        this.context = context;
    }

    public List<Category> getAllCategories() {
        List<Category> categoriesList = new Select().from(Category.class).execute();
        return categoriesList;
    }

    public void saveCategories(List<Category> categoryList) {
        ActiveAndroid.beginTransaction();
        for (Category category : categoryList) {
            category.save();
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }

    public Category getCategoryByServerId(String categoryServerId) {
        return new Select().from(Category.class).where(CATEGORY_SERVER_ID + " = ?", categoryServerId).executeSingle();
    }

}

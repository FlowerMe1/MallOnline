package com.mallonline.data;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.mallonline.models.CartOrder;
import com.mallonline.models.Category;
import com.mallonline.models.User;

/**
 * Created by Dina on 05/09/2016.
 */

public class ShoppingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initActiveAndroid();
    }

    private void initActiveAndroid() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(User.class);
        configurationBuilder.addModelClasses(Category.class);
        configurationBuilder.addModelClass(CartOrder.class);
        ActiveAndroid.initialize(configurationBuilder.create());
    }
}

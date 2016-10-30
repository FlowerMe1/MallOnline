package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mallonline.R;
import com.mallonline.data.SharedPref;
import com.mallonline.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        /*Remove status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                /*handle activity to another activity navigation view on UI thread.*/
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                navigateActivity();
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void navigateActivity() {
        if (SharedPref.LoadString(SplashActivity.this, SharedPref.USER_TOKEN) != null && !"".equals(SharedPref.LoadString(SplashActivity.this, SharedPref.USER_TOKEN).trim()))
            startActivityWithTransition(this, new Intent(this, HomeActivity.class));
        else
            startActivityWithTransition(this, new Intent(this, LoginActivity.class));
        finishActivity(this);
    }
}

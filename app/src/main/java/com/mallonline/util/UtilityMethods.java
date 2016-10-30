package com.mallonline.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mallonline.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Dina on 05/09/2016.
 */

public class UtilityMethods {
    public static byte[] createRandomKey(Random r) {
        byte[] key = new byte[16];
        for (int i = 0; i < 16; ++i) {
            int j = 97 + r.nextInt(122 - 97);
            char c = (char) j;
            key[i] = (byte) c;
        }
        return key;
    }

    public static Snackbar showSnackbar(final String snackbarMessage, View contentView, Context context) {
        Snackbar snackbar = Snackbar.make(contentView, snackbarMessage, Snackbar.LENGTH_SHORT);
        styleSnackbar(snackbar, context);
        hideKeypad(context);
        snackbar.show();
        return snackbar;
    }

    private static void styleSnackbar(Snackbar snackbar, Context context) {
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
        snackbar.setActionTextColor(Color.parseColor("#E6E6E6"));
        TextView actionTextView = (TextView) group.findViewById(R.id.snackbar_action);
        actionTextView.setTextSize(18);
        actionTextView.setTypeface(null, Typeface.BOLD);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
    }

      private static void hideKeypad(Context context) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int getBitmapWidth(Bitmap source, int targetHeight) {
        double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
        int targetWidth = (int) (targetHeight * aspectRatio);
        return targetWidth;
    }

    public static int getBitmapHeight(Bitmap source, int targetWidth){
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight  = (int) (targetWidth * aspectRatio);
        return targetHeight;
    }

    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if (bitmapToScale == null)
            return null;
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / width, newHeight / height);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
        return scaledBitmap;
    }

    public static ArrayList<String> getCountriesList() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        return countries;
    }
}

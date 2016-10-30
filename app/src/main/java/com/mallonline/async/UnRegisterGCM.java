package com.mallonline.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mallonline.data.SharedPref;

import java.io.IOException;

/**
 * Created by Dina on 18/09/2016.
 */

public class UnRegisterGCM extends AsyncTask {

    private GoogleCloudMessaging gcm;
    private Context context;

    public UnRegisterGCM(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if(SharedPref.LoadString(context, SharedPref.PROPERTY_REG_ID) == null || "".equals(SharedPref.LoadString(context, SharedPref.PROPERTY_REG_ID))) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                gcm.unregister();
                Log.i("unregister", "gcm unregister is called successfully");
//                InstanceID.getInstance(context).deleteInstanceID();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}

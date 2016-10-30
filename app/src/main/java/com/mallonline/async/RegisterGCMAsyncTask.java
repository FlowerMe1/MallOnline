package com.mallonline.async;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mallonline.api.ApiClient;
import com.mallonline.api.ClientCallback;
import com.mallonline.data.SharedPref;
import com.mallonline.models.Device;
import com.mallonline.models.ServerResponse;

import java.io.IOException;

/**
 * Created by Dina on 16/09/2016.
 */

public class RegisterGCMAsyncTask extends AsyncTask {

    private GoogleCloudMessaging gcm;
    private Context context;
    private String regid;
    public static String PROJECT_NUMBER = "973483363845";
    private ApiClient apiClient;
    private Device device;
    private boolean isRegistered = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public RegisterGCMAsyncTask(Context context, ApiClient apiClient) {
        this.context = context;
        this.apiClient = apiClient;
        this.device = new Device();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
//        if (checkPlayServices()) {
            try {
                if (gcm == null)
                    gcm = GoogleCloudMessaging.getInstance(context);//getApplicationContext()

                regid = getRegistrationId(context);

                if (regid.isEmpty())
                    registerGCM();
                else
                    isRegistered = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (!isRegistered) {
            apiClient.createDevice(device, new ClientCallback() {
                @Override
                public void onServerResultSuccess(Object o) {
                    SharedPref.SaveString(context, SharedPref.PROPERTY_REG_ID, regid);
                    SharedPref.SaveInt(context, SharedPref.PROPERTY_APP_VERSION, device.getOsVersion());
                }

                @Override
                public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {

                }
            });
        }
    }

    private void registerGCM() {
        try {
            regid = gcm.register(PROJECT_NUMBER);
            device.setRegId(regid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable((Activity)context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("", "This device is not supported - Google Play Services.");
            }
            return false;
        }
        return true;
    }

    /*check device registration*/
    private String getRegistrationId(Context context) {
        int currentVersion = getAppVersion(context);
        String registrationId = SharedPref.LoadString(context, SharedPref.PROPERTY_REG_ID);
        device.setOsVersion(currentVersion);
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = SharedPref.LoadInt(context, SharedPref.PROPERTY_APP_VERSION);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}

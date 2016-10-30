package com.mallonline.api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.mallonline.R;
import com.mallonline.custom.CustomJSONObjectRequest;
import com.mallonline.models.ServerResponse;

import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dina on 05/09/2016.
 */

public class VolleyHelper {

    protected Context context;
    protected RequestQueue mRequestQueue;
    public final static int NORMAL_TIME_OUT = 30000;

    protected VolleyHelper(Context context) {
        this.context = context;
        HttpsTrustManager.allowAllSSL();
        mRequestQueue = Volley.newRequestQueue(context);
    }

    protected void scheduleJsonObjectRequest(int request, String requestUrl, JSONObject jsonObject, final ClientCallback clientCallback, final Class<?> classType, int requestTimeout) {
        CustomJSONObjectRequest jsonObjReq = new CustomJSONObjectRequest(request,
                requestUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Object object = null;

                        if (classType != null && !(classType.equals(JSONObject.class))) {
                            object = getGsonObj(response.toString(), classType);
                        } else {
                            object = response;
                        }

                        clientCallback.onServerResultSuccess(object);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyServerError(error, clientCallback);
                    }
                });

        jsonObjReq.setRetryPolicy(new
                DefaultRetryPolicy(requestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonObjReq);
    }

    public static Object getGsonObj(String responseStr, Class<?> classType) {
        Gson gson = customizeGsonBuilder().create();
        return gson.fromJson(responseStr, classType);
    }

    private void volleyServerError(VolleyError error, ClientCallback clientCallback) {
        try {
            ServerResponse serverResponse = null;
            Gson gson = customizeGsonBuilder().create();
            String json;
            int responseStatusCode = 0;

            NetworkResponse response = error.networkResponse;
            if (response != null)
                responseStatusCode = response.statusCode;

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                serverResponse = new ServerResponse();
                serverResponse.setErrorMessageStr(context.getString(R.string.server_error));
            } else {
                if (response != null && response.data != null) {
                    json = new String(response.data);
                    serverResponse = gson.fromJson(json, ServerResponse.class);
                }

                if (serverResponse != null && serverResponse.getMessage() != null) {
                    if (serverResponse.getMessage().getClass().equals(LinkedTreeMap.class)) {
                        LinkedTreeMap responseMessagesLinkedTreeMap = (LinkedTreeMap) serverResponse.getMessage();
                        String errorMessageStr = "";
                        int index = 0;
                        Iterator messagesIterator = responseMessagesLinkedTreeMap.entrySet().iterator();
                        while (messagesIterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) messagesIterator.next();
                            if (index > 0)
                                errorMessageStr += " and ";
                            errorMessageStr += String.valueOf(pair.getValue()).replace("[", "").replace("]", "");
                            index++;
                        }
                        serverResponse.setErrorMessageStr(errorMessageStr);
                    } else if (serverResponse.getMessage().getClass().equals(String.class))
                        serverResponse.setErrorMessageStr(String.valueOf(serverResponse.getMessage()));
                }
            }

            clientCallback.onServerResultFailure(serverResponse, responseStatusCode);
        } catch (Exception e) {
            e.printStackTrace();
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.setErrorMessageStr(context.getString(R.string.something_wrong));
            clientCallback.onServerResultFailure(serverResponse, -1);//calling server failure with dummy data!!
        }
    }

    public static GsonBuilder customizeGsonBuilder() {

        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT,
                        Modifier.STATIC);
    }

    public void addToQueue(Request request, String tag) {
        request.setTag(tag);
        mRequestQueue.add(request);
    }
}

package com.mallonline.custom;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dina on 05/09/2016.
 */

public class CustomJSONObjectRequest extends JsonObjectRequest {

    /*
     * @param method: Request type(GET, PUT, DELETE or POST).
     * @param url: Request url.
     * @param jsonRequest: Request parameters.
     * @param successListener: Volley success response listener.
     * @param errorListener: Volley Failure response listener.
     * @param context: The application context that represent the current state
     * of the application.
     */
    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> successListener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, successListener, errorListener);
    }

    /*
     * Set request or API calls headers.
     *
     * @return headers: A hash map contains required headers.
     */
    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        /*A header that requests a "json" type and "utf-8" encoded response.*/
        headers.put("Content-Type", "application/json; charset=utf-8;");
        return headers;
    }
}

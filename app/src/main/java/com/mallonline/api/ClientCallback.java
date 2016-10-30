package com.mallonline.api;

import com.mallonline.models.ServerResponse;

/**
 * Created by Dina on 05/09/2016.
 */

public interface ClientCallback<T> {

    void onServerResultSuccess(T t);

    void onServerResultFailure(ServerResponse serverResponse, int statusCode);

}

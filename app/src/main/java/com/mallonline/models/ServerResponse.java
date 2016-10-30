package com.mallonline.models;

/**
 * Created by Dina on 05/09/2016.
 */

public class ServerResponse {
    private boolean success;

    private Object message;

    private String errorMessageStr;

    private Error[] errors;

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrors(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public String getErrorMessageStr() {
        return errorMessageStr;
    }

    public void setErrorMessageStr(String errorMessageStr) {
        this.errorMessageStr = errorMessageStr;
    }
}

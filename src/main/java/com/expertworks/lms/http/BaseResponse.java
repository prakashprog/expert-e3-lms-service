package com.expertworks.lms.http;

public class BaseResponse {

    private ResponseCode responseCode;
    private String description;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum ResponseCode {
        SUCCESS,FAIL;
    }
}

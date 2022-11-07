package com.hermes.hermesapi.models;

public class ExceptionModel {
    private String message;
    private Integer statusCode;

    public ExceptionModel(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Integer getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}

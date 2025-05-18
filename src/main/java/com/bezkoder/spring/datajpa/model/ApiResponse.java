package com.bezkoder.spring.datajpa.model;

public class ApiResponse<T> {
    private String code;
    private String message;
    private String request_id;
    private T response;

    public ApiResponse() {
    }

    public ApiResponse(String code, String message, String request_id, T body) {
        this.code = code;
        this.message = message;
        this.request_id = request_id;
        this.response = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}

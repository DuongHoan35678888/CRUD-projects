package com.bezkoder.spring.datajpa.dto;

public class ApiResponse<T> {
    private String code;
    private String request_id;
    private T response;

    public ApiResponse() {
    }

    public ApiResponse(String code, String request_id, T body) {
        this.code = code;
        this.request_id = request_id;
        this.response = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

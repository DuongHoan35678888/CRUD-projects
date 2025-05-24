package com.bezkoder.spring.datajpa.dto;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class ApiResponse<T> {
    private String code;

    @JsonProperty("request_id")
    private String requestId;

    private T response;

    public ApiResponse() {
    }

    public ApiResponse(String code, String requestId, T response) {
        this.code = code;
        this.requestId = requestId;
        this.response = response;
    }

    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>(ResponseCode.SUCCESS, UUID.randomUUID().toString(), response);
    }

    public static <T> ApiResponse<T> error(String errorCode, T response) {
        return new ApiResponse<>(errorCode, UUID.randomUUID().toString(), response);
    }

}

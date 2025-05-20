package com.bezkoder.spring.datajpa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
}

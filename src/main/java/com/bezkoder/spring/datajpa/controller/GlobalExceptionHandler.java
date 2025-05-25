package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.ExceptionResponse;
import com.bezkoder.spring.datajpa.exception.BusinessException;
import com.bezkoder.spring.datajpa.exception.UserNotFoundException;
import com.bezkoder.spring.datajpa.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Users>> handleUserNotFound(UserNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Users> response = new ApiResponse<>(
                ResponseCode.USER_NOT_FOUND,
                requestId,
                new Users()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Xử lý lỗi validation hoặc lỗi input không hợp lệ (ví dụ)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<String> response = new ApiResponse<>(
                ResponseCode.INVALID_INPUT,
                requestId,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý lỗi chung, chưa được xử lý ở trên
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ExceptionResponse>> handleException(Exception ex) {
        String requestId = UUID.randomUUID().toString();
        log.error("Internal server error, requestId: {}, error: {}", requestId, ex.getMessage(), ex);
        ApiResponse<ExceptionResponse> response = new ApiResponse<>(
                ResponseCode.INTERNAL_SERVER_ERROR,
                requestId,
                new ExceptionResponse(ResponseCode.INTERNAL_SERVER_ERROR)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String requestId = UUID.randomUUID().toString();
        String errorMsg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiResponse<String> response = new ApiResponse<>(
                ResponseCode.INVALID_INPUT,
                requestId,
                errorMsg
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ExceptionResponse>> handleBusinessException(BusinessException ex) {
        String requestId = UUID.randomUUID().toString();
        log.warn("Business exception, requestId: {}, code: {}, message: {}", requestId, ex.getCode(), ex.getMessage());

        ApiResponse<ExceptionResponse> response = new ApiResponse<>(
                ex.getCode(),  // mã lỗi định nghĩa sẵn trong enum ResponseCode
                requestId,
                new ExceptionResponse(ex.getCode())
        );

        HttpStatus status;

        // Gắn mã HTTP phù hợp theo loại lỗi
        switch (ex.getCode()) {
            case ResponseCode.INCORRECT_ACCOUNT_OR_PASSWORD:
                status = HttpStatus.BAD_REQUEST; // 400
                break;
            case ResponseCode.USER_DOES_NOT_EXIST:
            case ResponseCode.ID_NOT_EXISTS:
                status = HttpStatus.NOT_FOUND; // 404
                break;
            case ResponseCode.REFRESH_TOKEN_DOES_NOT_EXIST:
            case ResponseCode.REFRESH_TOKEN_EXPIRED:
            case ResponseCode.UNAUTHORIZED:
                status = HttpStatus.UNAUTHORIZED;
                break;
            default:
                status = HttpStatus.BAD_REQUEST; // fallback
        }

        return ResponseEntity.status(status).body(response);
    }

}

package com.bezkoder.spring.datajpa.common;

public class ResponseCode {

    public static final String USER_NOT_FOUND = "AUT002";
    public static final String INVALID_PASSWORD = "200002";
    public static final String SUCCESS = "200000";
    public static final String LOGGED_OUT_SUCCESS = "200005";

    public static final String INCORRECT_ACCOUNT_OR_PASSWORD = "AUT001";
    public static final String INVALID_INPUT = "100001";
    public static final String ERROR = "404";
    public static final String REFRESH_TOKEN_DOES_NOT_EXIST = "Refresh token does not exist";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String USER_ALREADY_EXISTS = "AUT004";
    public static final String INTERNAL_SERVER_ERROR = "AUT005";
    public static final String VALIDATION_FAILED = "AUT006";
    public static final String REFRESH_TOKEN_EXPIRED = "AUT007";
    public static final String UNAUTHORIZED = "AUT008";
    public static final String ID_NOT_EXISTS = "FK0001";
}

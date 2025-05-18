package com.bezkoder.spring.datajpa.model;

public class UserLogin {
    private String session_id;
    private String access_token;
    private String user_name;

    public UserLogin() {
    }

    public UserLogin(String session_id, String access_token_expires, String user_name) {
        this.session_id = session_id;
        this.access_token = access_token_expires;
        this.user_name = user_name;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}

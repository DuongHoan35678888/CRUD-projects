package com.bezkoder.spring.datajpa.model;

public class UserLogin {
    private String session_id;
    private String access_token_expires;
    private String user_name;

    public UserLogin() {
    }

    public UserLogin(String session_id, String access_token_expires, String user_name) {
        this.session_id = session_id;
        this.access_token_expires = access_token_expires;
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

    public String getAccess_token_expires() {
        return access_token_expires;
    }

    public void setAccess_token_expires(String access_token_expires) {
        this.access_token_expires = access_token_expires;
    }
}

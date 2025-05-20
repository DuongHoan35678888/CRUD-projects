package com.bezkoder.spring.datajpa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_token_expires")
    private long accessTokenExpires;

    @JsonProperty("user_name")
    private String userName;
}


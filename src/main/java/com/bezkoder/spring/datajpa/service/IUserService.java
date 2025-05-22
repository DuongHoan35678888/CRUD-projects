package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.RefreshTokenRequest;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {
    List<Users> getAllUser();

    Users register(Users users);

    Authentication verify(Users users);

    Users registerUser(Users users);

    String getSalt(String username);

    ResponseEntity<ApiResponse<UserLogin>> login(Users users);

    boolean existsByUsername(String username);

    ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApiResponse<UserLogin>> refreshToken(RefreshTokenRequest request);
}

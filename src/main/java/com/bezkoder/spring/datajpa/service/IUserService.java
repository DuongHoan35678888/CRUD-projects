package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.BooleanResponse;
import com.bezkoder.spring.datajpa.dto.RefreshTokenRequest;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
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

    ResponseEntity<ApiResponse<Boolean>> logoutv1(RefreshTokenRequest request);

    ResponseEntity<ApiResponse<UserLogin>> refreshToken(RefreshTokenRequest request);

    void validateUsernameNotTaken(@NotBlank(message = "Username must not be blank") String username);

    ResponseEntity<ApiResponse<BooleanResponse>> logout(HttpServletRequest request);
}

package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.RefreshTokenRequest;
import com.bezkoder.spring.datajpa.dto.SaltResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.exception.UserNotFoundException;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.RefreshTokenRepository;
import com.bezkoder.spring.datajpa.service.IUserService;
import com.bezkoder.spring.datajpa.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    JWTService jwtService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/users")
    public List<Users> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/register/v1")
    public ResponseEntity<ApiResponse<Users>> register(@Valid @RequestBody Users users) {
        String requestId = UUID.randomUUID().toString();

        // Check username đã tồn tại
        if (userService.existsByUsername(users.getUsername())) {
            throw new IllegalArgumentException(ResponseCode.USER_ALREADY_EXISTS);
        }

        Users registeredUser = userService.register(users);
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.SUCCESS, requestId, registeredUser));
    }

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users users) {
        return userService.registerUser(users);
    }

    @GetMapping("/salt")
    public ResponseEntity<ApiResponse<SaltResponse>> getSalt(@RequestParam(required = false) String username) {
        String requestId = UUID.randomUUID().toString();

        if (username == null || username.isBlank()) {
            // Trả về object rỗng thay vì null
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ResponseCode.INVALID_INPUT, requestId, new SaltResponse()));
        }

        String salt;
        try {
            salt = userService.getSalt(username); // Có thể ném UserNotFoundException
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ResponseCode.USER_NOT_FOUND, requestId, new SaltResponse()));
        }

        SaltResponse saltResponse = new SaltResponse(salt);
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.SUCCESS, requestId, saltResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLogin>> login(@RequestBody Users loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request,
                                                       HttpServletResponse response) {
        return userService.logout(request, response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<UserLogin>> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return userService.refreshToken(request);
    }

}

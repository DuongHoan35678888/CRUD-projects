package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.model.ApiResponse;
import com.bezkoder.spring.datajpa.model.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    IUserService service;

    @GetMapping("/users")
    public List<Users> getAllUser() {
        return service.getAllUser();
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/register/v1")
    public Users register(@RequestBody Users users) {
        return service.register(users);
    }

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users users) {
        return service.registerUser(users);
    }

    @PostMapping("/salt")
    public String getSalt(String username) {
        return service.getSalt(username);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLogin>> login(@RequestBody Users users, HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString(); // Tạo ID request ngẫu nhiên

        try {
            String token = String.valueOf(service.verify(users));
            UserLogin userLogin = new UserLogin(requestId, token, users.getUsername());
            ApiResponse<UserLogin> response = new ApiResponse<>(
                    "200",                         // code
                    "Đăng nhập thành công",        // message
                    requestId,                     // request_id
                    userLogin                          // body
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<UserLogin> errorResponse = new ApiResponse<>(
                    "401",
                    "Đăng nhập thất bại: " + e.getMessage(),
                    requestId,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

}

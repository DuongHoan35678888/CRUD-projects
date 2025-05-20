package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.SaltResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.service.IUserService;
import com.bezkoder.spring.datajpa.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    IUserService service;

    @Autowired
    JWTService jwtService;

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

    @GetMapping("/salt")
    public ResponseEntity<ApiResponse<SaltResponse>> getSalt(@RequestParam String username) {
        String requestId = UUID.randomUUID().toString();

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ResponseCode.INVALID_INPUT, requestId, null));
        }

        String salt = service.getSalt(username);  // Nếu user không tồn tại, ném UserNotFoundException

        SaltResponse saltResponse = new SaltResponse(salt);
        ApiResponse<SaltResponse> response = new ApiResponse<>(ResponseCode.SUCCESS, requestId, saltResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLogin>> login(@RequestBody Users users) {
        return service.login(users);
    }

}

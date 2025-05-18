package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.service.IUserService;
import com.bezkoder.spring.datajpa.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/salt")
    public String getSalt(String username) {
        return service.getSalt(username);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLogin>> login(@RequestBody Users users) {
        return service.login(users);
    }

}

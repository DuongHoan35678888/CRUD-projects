package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String login(@RequestBody Users users) {
        return service.verify(users);
    }
}

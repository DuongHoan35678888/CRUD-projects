package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

    @PostMapping("/register")
    public Users register(@RequestBody Users users) {
        return service.register(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users) {
        return service.verify(users);
    }
}

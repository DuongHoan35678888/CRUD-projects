package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public List<Users> getAllUser();

    public Users register(Users users);

    String verify(Users users);

    Users registerUser(Users users);

    String getSalt(String username);

    ResponseEntity<ApiResponse<UserLogin>> login(Users users);
}

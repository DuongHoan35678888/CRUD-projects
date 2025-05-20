package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.exception.UserNotFoundException;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public Users register(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return repository.save(users);
    }

    @Override
    public String verify(Users users) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getUsername());
        } else {
            return "fail";
        }

    }

    @Override
    public Users registerUser(Users users) {
        if (users == null) {
            throw new UsernameNotFoundException("Thong tin dang ky khong hop le");
        }
        return repository.save(users);
    }

    @Override
    public String getSalt(String username) {
        return repository.findSaltByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    @Override
    public ResponseEntity<ApiResponse<UserLogin>> login(Users users) {
        String requestId = UUID.randomUUID().toString();

        // Giả định verify(users) ném UserNotFoundException hoặc các exception khác nếu sai
        String token = String.valueOf(verify(users));
        String tokenExpires = jwtService.generateToken(users.getUsername());
        Date expiration = jwtService.extractExpiration(tokenExpires);
        int expirationTime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);

        UserLogin userLogin = new UserLogin(requestId, token, expirationTime, users.getUsername());
        ApiResponse<UserLogin> response = new ApiResponse<>(
                ResponseCode.SUCCESS,
                requestId,
                userLogin
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public List<Users> getAllUser() {
        return repository.findAll();
    }
}

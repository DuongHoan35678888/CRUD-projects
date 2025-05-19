package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return repository.findSaltByUsername(username);
    }

    @Override
    public ResponseEntity<ApiResponse<UserLogin>> login(Users users) {
        String requestId = UUID.randomUUID().toString(); // Tạo ID request ngẫu nhiên

        try {
            String token = String.valueOf(verify(users));

            String token_expires = jwtService.generateToken(users.getUsername());
            Date expiration = jwtService.extractExpiration(token_expires);
            int expirationTime = (int)((expiration.getTime() - System.currentTimeMillis()) / 1000);

            UserLogin userLogin = new UserLogin(requestId, token, expirationTime, users.getUsername());
            ApiResponse<UserLogin> response = new ApiResponse<>(
                    ResponseCode.SUCCESS,
                    requestId,
                    userLogin
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<UserLogin> errorResponse = new ApiResponse<>(
                    ResponseCode.USER_NOT_FOUND,
                    requestId,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @Override
    public List<Users> getAllUser() {
        return repository.findAll();
    }
}

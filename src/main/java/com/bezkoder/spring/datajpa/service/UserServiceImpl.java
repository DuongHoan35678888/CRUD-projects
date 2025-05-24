package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.RefreshTokenRequest;
import com.bezkoder.spring.datajpa.dto.UserLogin;
import com.bezkoder.spring.datajpa.entity.BlacklistedToken;
import com.bezkoder.spring.datajpa.exception.BusinessException;
import com.bezkoder.spring.datajpa.exception.UserNotFoundException;
import com.bezkoder.spring.datajpa.model.RefreshToken;
import com.bezkoder.spring.datajpa.model.RefreshTokenStatus;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.BlacklistedTokenRepository;
import com.bezkoder.spring.datajpa.repository.RefreshTokenRepository;
import com.bezkoder.spring.datajpa.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public Users register(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepository.save(users);
    }

    @Override
    public Authentication verify(Users users) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword())
            );
            return authentication;
        } catch (AuthenticationException ex) {
            // Gói exception thành BusinessException để trả về lỗi 401
            throw new BusinessException(ResponseCode.INCORRECT_ACCOUNT_OR_PASSWORD, "Incorrect account or password");
        }
    }

    @Override
    public Users registerUser(Users users) {
        if (users == null) {
            throw new UsernameNotFoundException(ResponseCode.VALIDATION_FAILED);
        }
        return userRepository.save(users);
    }

    @Override
    public String getSalt(String username) {
        return userRepository.findSaltByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    @Override
    public ResponseEntity<ApiResponse<UserLogin>> login(Users users) {
        String requestId = UUID.randomUUID().toString();

        // 1. Xác thực người dùng
        Authentication authentication = verify(users);
        if (!authentication.isAuthenticated()) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED, "Người dùng chưa được xác thực");
        }

        // 2. Lấy thông tin người dùng đã xác thực
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 3. Tạo access token (JWT)
        String accessToken = jwtService.generateToken(username);
        Date expiration = jwtService.extractExpiration(accessToken);
        int expirationTime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);

        // 4. Tạo refresh token
        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken.setRefreshTokenStatus(RefreshTokenStatus.ACTIVE);

        // Gán user từ DB để đảm bảo đầy đủ (nếu cần)
        refreshToken.setUser(userRepository.findByUsername(username));
        refreshTokenRepository.save(refreshToken);

        // 5. Tạo response
        UserLogin userLogin = new UserLogin(
                requestId,
                accessToken,
                expirationTime,
                username,
                refreshTokenString
        );

        ApiResponse<UserLogin> response = new ApiResponse<>(
                ResponseCode.SUCCESS,
                requestId,
                userLogin
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Boolean>> logoutv1(RefreshTokenRequest request) {
        String refreshTokenStr = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new BusinessException(ResponseCode.ERROR, ResponseCode.REFRESH_TOKEN_DOES_NOT_EXIST));

        refreshToken.setRefreshTokenStatus(RefreshTokenStatus.REVOKED);
        refreshTokenRepository.save(refreshToken);

        log.info("Refresh token '{}' has been revoked", refreshTokenStr);

        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @Override
    public ResponseEntity<ApiResponse<UserLogin>> refreshToken(RefreshTokenRequest request) {
        String requestId = UUID.randomUUID().toString();

        // Tìm refresh token trong DB
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ResponseCode.ERROR, ResponseCode.REFRESH_TOKEN_DOES_NOT_EXIST));

        // Kiểm tra trạng thái và hạn
        if (token.getRefreshTokenStatus() != RefreshTokenStatus.ACTIVE || token.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(ResponseCode.ERROR, ResponseCode.REFRESH_TOKEN_EXPIRED);
        }

        // Tạo access token mới
        String username = token.getUser().getUsername();
        String newAccessToken = jwtService.generateToken(username);
        Date expiration = jwtService.extractExpiration(newAccessToken);
        int expirationTime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);

        // Trả về DTO giống login
        UserLogin userLogin = new UserLogin(
                requestId,
                newAccessToken,
                expirationTime,
                username,
                token.getToken() // Giữ nguyên refresh token cũ
        );

        ApiResponse<UserLogin> response = new ApiResponse<>(ResponseCode.SUCCESS, requestId, userLogin);
        return ResponseEntity.ok(response);
    }

    @Override
    public void validateUsernameNotTaken(String username) {
        if (username == null || username.isBlank()) {
            throw new BusinessException(ResponseCode.INVALID_INPUT, "Username is required");
        }
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(ResponseCode.USER_ALREADY_EXISTS, "Username already exists");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED, "Token is blacklisted");
        }

        Date expiry = jwtService.extractExpiration(token);

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiry.toInstant());

        blacklistedTokenRepository.save(blacklistedToken);

        return ResponseEntity.ok(ApiResponse.success(true));
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new BusinessException(ResponseCode.UNAUTHORIZED, ResponseCode.REFRESH_TOKEN_DOES_NOT_EXIST);
    }

    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }
}

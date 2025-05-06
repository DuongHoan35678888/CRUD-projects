package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Users> getAllUser() {
        return repository.findAll();
    }
}

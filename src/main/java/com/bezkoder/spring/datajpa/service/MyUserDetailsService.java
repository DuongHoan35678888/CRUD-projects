package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.model.UserPrincipal;
import com.bezkoder.spring.datajpa.model.Users;
import com.bezkoder.spring.datajpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println(ResponseCode.USER_NOT_FOUND);
            throw new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND);
        }

        return new UserPrincipal(user);
    }
}

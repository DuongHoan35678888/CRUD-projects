package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.Users;

import java.util.List;

public interface IUserService {
    public List<Users> getAllUser();

    public Users register(Users users);

    String verify(Users users);
}

package com.epam.jmp.rest.service;

import com.epam.jmp.rest.model.User;

import java.util.List;

public interface UserService {

    Long createUser(User user);

    void updateUser(User user);

    List<User> findAll();

    void delete(Long userId);
}

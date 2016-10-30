package com.epam.jmp.rest.service.impl;

import com.epam.jmp.rest.model.User;
import com.epam.jmp.rest.repository.UserRepository;
import com.epam.jmp.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long createUser(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }
}

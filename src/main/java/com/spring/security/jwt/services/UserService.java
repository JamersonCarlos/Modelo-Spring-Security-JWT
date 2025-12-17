package com.spring.security.jwt.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.security.jwt.entities.User;
import com.spring.security.jwt.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> isUserActive(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

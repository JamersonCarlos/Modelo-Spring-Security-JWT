package com.spring.security.jwt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.security.jwt.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    
    public Boolean isUserActive(String username) {
        return userRepository.findByUsername(username) != null;
            
    }
}

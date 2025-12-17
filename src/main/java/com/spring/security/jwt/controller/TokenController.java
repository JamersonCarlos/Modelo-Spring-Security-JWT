package com.spring.security.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.jwt.dto.LoginRequest;
import com.spring.security.jwt.dto.LoginResponse;

@RestController
public class TokenController {
    
    @Autowired
    private JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> generateToken(@RequestBody LoginRequest loginRequest) {
        return ""
    }

}

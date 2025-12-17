package com.spring.security.jwt.dto;

public record LoginRequest(
    String username,
    String password
) {
    
}

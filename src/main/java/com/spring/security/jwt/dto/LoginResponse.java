package com.spring.security.jwt.dto;

public record LoginResponse(
    String accessToken, String expiresIn 
) {

}

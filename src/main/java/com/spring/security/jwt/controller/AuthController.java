package com.spring.security.jwt.controller;

import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.spring.security.jwt.dto.LoginRequest;
import com.spring.security.jwt.dto.LoginResponse;
import com.spring.security.jwt.entities.User;
import com.spring.security.jwt.services.UserService;

@RestController("/auth")
public class AuthController {

  @Autowired
  private JwtEncoder jwtEncoder;

  @Autowired
  private UserService userService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> generateToken(@RequestBody LoginRequest loginRequest) {

    Optional<User> userOpt = userService.isUserActive(loginRequest.username());
    if (userOpt.isEmpty() || !userOpt.get().isLoginCorrect(loginRequest, passwordEncoder)) {
      throw new BadCredentialsException("user or password is invalid!");
    }

    User user = userOpt.get();
    var now = Instant.now();
    var expiresIn = 300L;

    var claims = JwtClaimsSet.builder().issuer("mybackend").subject(user.getUsername())
        .expiresAt(now.plusSeconds(expiresIn)).issuedAt(now).build();

    var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

  }


}

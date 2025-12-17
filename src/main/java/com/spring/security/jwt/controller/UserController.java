package com.spring.security.jwt.controller;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.spring.security.jwt.dto.RegisterRequest;
import com.spring.security.jwt.entities.Role;
import com.spring.security.jwt.entities.User;
import com.spring.security.jwt.repository.RoleRepository;
import com.spring.security.jwt.services.UserService;
import jakarta.transaction.Transactional;

@RestController("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired 
  private RoleRepository roleRepository; 

  @Transactional
  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest registerRequest) {
    
    Optional<Role> optRole = roleRepository.findByName("BASIC"); 
    Role role = optRole.orElseThrow(() -> new RuntimeException( "Role not found"));

    Optional<User> existingUser = userService.isUserActive(registerRequest.username());
    if (existingUser.isPresent()) {
      return ResponseEntity.status(409).build(); // Conflict if user already exists
    }

    User newUser = new User();
    newUser.setUsername(registerRequest.username());
    newUser.setPassword(registerRequest.password());
    newUser.setRoles(Set.of(role));

    userService.saveUser(newUser);

    return ResponseEntity.ok().build();
  }
}

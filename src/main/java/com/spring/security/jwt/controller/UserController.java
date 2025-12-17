package com.spring.security.jwt.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.security.jwt.dto.RegisterRequest;
import com.spring.security.jwt.entities.Role;
import com.spring.security.jwt.entities.User;
import com.spring.security.jwt.repository.RoleRepository;
import com.spring.security.jwt.services.UserService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Transactional
  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest registerRequest) {

    Optional<Role> optRole = roleRepository.findByName("BASIC");
    Role role = optRole.orElseThrow(() -> new RuntimeException("Role not found"));

    Optional<User> existingUser = userService.isUserActive(registerRequest.username());
    if (existingUser.isPresent()) {
      return ResponseEntity.status(409).build(); // Conflict if user already exists
    }

    User newUser = new User();
    newUser.setUsername(registerRequest.username());
    newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
    newUser.setRoles(Set.of(role));

    userService.saveUser(newUser);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}

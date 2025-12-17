package com.spring.security.jwt.config;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.spring.security.jwt.entities.Role;
import com.spring.security.jwt.entities.User;
import com.spring.security.jwt.repository.RoleRepository;
import com.spring.security.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;


  @Override
  @Transactional
  public void run(String... args) throws Exception {
    Optional<Role> adminRole = roleRepository.findByName("ADMIN");

    if (adminRole.isEmpty()) {
      Role role = new Role();
      role.setName("ADMIN");
      roleRepository.save(role);

      adminRole = Optional.of(role);
    }

    Role role = adminRole.get();

    var userAdmin = userRepository.findByUsername("admin");

    if (userAdmin.isEmpty()) {
      User user = new User();
      user.setUsername("admin");
      user.setPassword(passwordEncoder.encode("Sictec@135792468"));
      user.setRoles(Set.of(role));
      userRepository.save(user);
    } else {
      System.out.println("Admin user already exists");
    }

  }
}

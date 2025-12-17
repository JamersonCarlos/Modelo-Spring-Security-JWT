package com.spring.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.security.jwt.entities.User;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
}

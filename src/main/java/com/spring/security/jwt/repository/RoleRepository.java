package com.spring.security.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.security.jwt.entities.Role;

@Repository
public interface  RoleRepository extends JpaRepository<Role, Long> {
    
}

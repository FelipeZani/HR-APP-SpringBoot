package com.hrapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrapp.domain.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    List<User> findByRole(String role);
    List<User> findByRoleIn(List<String> roles);
}

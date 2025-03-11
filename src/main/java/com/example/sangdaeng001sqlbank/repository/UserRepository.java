package com.example.sangdaeng001sqlbank.repository;

import com.example.sangdaeng001sqlbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByUsernameAndNameAndEmail(String username, String name, String email);
}

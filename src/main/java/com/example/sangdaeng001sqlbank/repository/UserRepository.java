package com.example.sangdaeng001sqlbank.repository;

import com.example.sangdaeng001sqlbank.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByUsernameAndNameAndEmail(String username, String name, String email);
    // ROLE_ADMIN을 제외한 전체 유저 리스트
    List<User> findByRoleNotOrderByUserIdDesc(String role);

    Page<User> findByRoleNot(String role, Pageable pageable);

    // 특정 기수(classNum)에서 ROLE_ADMIN을 제외한 유저 리스트
    List<User> findByClassNumAndRoleNotOrderByUserIdDesc(int classNum, String role);

}

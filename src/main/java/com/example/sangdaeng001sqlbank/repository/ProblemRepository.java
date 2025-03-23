package com.example.sangdaeng001sqlbank.repository;


import com.example.sangdaeng001sqlbank.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    List<Problem> findByIdContaining(String keyword);
    List<Problem> findByQuestionContaining(String keyword);
    List<Problem> findByCreatedByNameContaining(String keyword);
}

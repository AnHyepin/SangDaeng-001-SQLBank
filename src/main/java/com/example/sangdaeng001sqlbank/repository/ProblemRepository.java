package com.example.sangdaeng001sqlbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sangdaeng001sqlbank.entity.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    // ID로 검색 (LIKE 검색)
    @Query("SELECT p FROM Problem p WHERE CAST(p.problemId AS string) LIKE %:keyword%")
    List<Problem> findByIdContaining(@Param("keyword") String keyword);

    // 문제 제목으로 검색
    List<Problem> findByQuestionContaining(String keyword);

    // 작성자 이름으로 검색
    @Query("SELECT p FROM Problem p JOIN p.createdBy u WHERE u.name LIKE %:keyword%")
    List<Problem> findByCreatedByNameContaining(@Param("keyword") String keyword);
}

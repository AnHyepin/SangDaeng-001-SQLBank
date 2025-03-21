package com.example.sangdaeng001sqlbank.repository;

import com.example.sangdaeng001sqlbank.entity.ClassSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSettingRepository extends JpaRepository<ClassSetting, Integer> {
    @Query("SELECT c.classNum FROM ClassSetting c WHERE c.id = 1")
    Integer findClassNum();
}
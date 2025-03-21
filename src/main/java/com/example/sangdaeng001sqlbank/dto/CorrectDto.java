package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class CorrectDto {
    private String difficulty;   // 난이도 (EASY, MEDIUM, HARD)
    private int totalAttempts;   // 전체 시도 횟수
    private int correctAttempts; // 정답 횟수
    private double correctRate;  // 정답률 (%)
}

package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class WrongRateDto {
    private String difficulty;   // 문제 난이도 (HARD)
    private Long problemId;      // 문제 ID
    private String question;     // 문제 제목
    private Long wrongAttempts;  // 틀린 횟수
    private Long totalAttempts;  // 전체 시도 횟수
    private Double wrongRate;    // 오답률 (%)
}
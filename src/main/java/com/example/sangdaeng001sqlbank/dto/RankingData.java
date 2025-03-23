package com.example.sangdaeng001sqlbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingData {
    private Long userId;
    private String userName;
    private int totalAttempts;
    private int correctAnswers;
    private int easyCount;
    private int mediumCount;
    private int hardCount;
    private String difficulty;
}

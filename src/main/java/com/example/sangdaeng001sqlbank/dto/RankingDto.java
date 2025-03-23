package com.example.sangdaeng001sqlbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {
    private String name;
    private int score;
    private int correctRate;
    private int attempts;
    private String difficulty;
}
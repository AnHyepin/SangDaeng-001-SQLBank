package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class ScoreChangeDto {
    private Integer userId;
    private String username;
    private String name;
    private String difficulty;
    private int score;
    private int round;
}

package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class AverageScore {

    private Integer userId;
    private String username;
    private String name;
    private String difficulty;
    private double avgScore;
}

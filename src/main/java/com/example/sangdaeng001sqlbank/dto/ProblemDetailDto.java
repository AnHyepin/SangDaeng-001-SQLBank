package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class ProblemDetailDto {
    private int problemId;
    private String question;
    private String selectedChoiceText;
    private String correctChoiceText;
    private int isCorrect;
}

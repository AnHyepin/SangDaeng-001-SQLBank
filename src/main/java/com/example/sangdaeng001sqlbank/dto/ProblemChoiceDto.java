package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class ProblemChoiceDto {
    private int choiceId; // 보기 ID
    private int problemId; // 문제 ID
    private String choiceText; // 보기 내용
    private int isCorrect; // 정답 여부 (true = 정답, false = 오답)
}

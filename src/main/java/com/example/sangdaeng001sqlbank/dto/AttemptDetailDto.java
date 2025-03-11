package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class AttemptDetailDto {
    private int attemptId; // 풀이 상세 ID
    private int sessionId; // 회차 ID
    private int problemId; // 문제 ID
    private int selectedChoiceId; // 사용자가 선택한 보기 ID (NULL 가능)
    private String selectedChoiceText; // 선택 답
    private String correctChoiceText; // 정답
    private int isCorrect; // 정답 여부 (true = 정답, false = 오답)
}

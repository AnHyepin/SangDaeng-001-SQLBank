package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class AttemptSessionDto {
    private int sessionId; // 풀이 회차 고유 ID
    private int userId; // 사용자 ID
    private String startedAt; // 풀이 시작 시간
    private String finishedAt; // 풀이 종료 시간 (NULL 가능)
    private int totalScore; // 해당 회차의 점수 (NULL 가능)
    private String difficulty; // 난이도
    private int times; // 회차
}


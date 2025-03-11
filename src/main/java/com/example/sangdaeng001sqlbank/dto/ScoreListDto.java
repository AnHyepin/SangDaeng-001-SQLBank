package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScoreListDto {
    private int sessionId;        // 풀이 회차 ID
    private int totalScore;       // 총 점수
    private String difficulty;    // 난이도 (초급, 중급, 고급)
    private List<AttemptDetailDto> attemptDetails; // 해당 회차의 문제별 정답 데이터
    private int times; // 회차
}
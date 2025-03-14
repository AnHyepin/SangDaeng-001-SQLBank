package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProblemDto {
    private int problemId; // 문제 ID
    private String question; // 문제 질문
    private String description; // 문제 설명
    private String difficulty; // 난이도 (EASY, MEDIUM, HARD)
    private int createdBy; // 출제자 ID
    private String createdByName; // 출제자 이름
    private String createdAt; // 출제일
    private char permitYn;
    private String answer;

    private List<ProblemChoiceDto> choices; // 보기들
}

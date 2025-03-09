package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class SuggestionDto {
    private int suggestionId; // 건의 ID
    private int problemId; // 문제 ID
    private int userId; // 사용자 ID
    private String suggestionType; // 건의 유형 (오탈자 / 틀린 정답)
    private String content; // 건의 내용
    private String createdAt; // 작성일
}

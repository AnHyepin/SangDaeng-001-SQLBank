package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class ProblemDiscussionDto {
    private int discussionId; // 의견 ID
    private int problemId; // 문제 ID
    private int userId; // 작성자 ID
    private String content; // 의견 내용
    private String createdAt; // 작성일
}

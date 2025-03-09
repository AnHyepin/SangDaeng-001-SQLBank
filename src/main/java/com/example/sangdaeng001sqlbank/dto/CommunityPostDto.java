package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class CommunityPostDto {
    private int postId; // 게시글 ID
    private int userId; // 작성자 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String createdAt; // 작성일
}


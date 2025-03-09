package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class CommentDto {
    private int commentId; // 댓글 ID
    private int postId; // 게시글 ID
    private int userId; // 작성자 ID
    private String content; // 댓글 내용
    private String createdAt; // 작성일
}

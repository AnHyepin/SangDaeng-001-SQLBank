package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

@Data
public class UserDto {
    private int userId; // 사용자 ID (고유)
    private String username; // 사용자 아이디
    private String name; // 사용자 이름
    private String password; // 비밀번호
    private String email; // 이메일
    private String role; // 사용자 역할 (일반/관리자  )
    private String createdAt; // 가입일
}

package com.example.sangdaeng001sqlbank.dto;

import com.example.sangdaeng001sqlbank.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer userId; // 사용자 ID (고유)
    private String username; // 사용자 아이디
    private String name; // 사용자 이름
    private String password; // 비밀번호
    private String email; // 이메일
    private String role; // 사용자 역할 (일반/관리자  )
    private String createdAt; // 가입일


}

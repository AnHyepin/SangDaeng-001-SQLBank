package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.service.hyepin.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<?> register(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        try {
            String msg = loginService.registerUser(userDto);

            if ("중복된 ID입니다.".equals(msg) || "중복된 email입니다.".equals(msg)) {
                return ResponseEntity.badRequest().body(msg); // 400 상태코드 + 메시지 반환
            }

            return ResponseEntity.ok(msg); // 200 상태코드 + "회원가입 성공" 메시지 반환
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //로그인
    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            String token = loginService.login(userDto);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

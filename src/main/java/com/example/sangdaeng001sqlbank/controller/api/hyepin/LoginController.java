package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.jwt.JwtCookieUtil;
import com.example.sangdaeng001sqlbank.jwt.JwtTokenProvider;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import com.example.sangdaeng001sqlbank.service.hyepin.LoginService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
@Slf4j
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final JwtCookieUtil jwtCookieUtil;

    @Value("${jwt.access-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private int refreshTokenExpiration;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> register(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        try {
            String msg = loginService.registerUser(userDto);

            if ("중복된 ID입니다.".equals(msg) || "중복된 email입니다.".equals(msg)) {
                return ResponseEntity.badRequest().body(Map.of("message", msg)); // 400 상태코드 + 메시지 반환
            }

            return ResponseEntity.ok().body(Map.of("message", msg)); // 200 상태코드 + "회원가입 성공" 메시지 반환
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류"));
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute UserDto userDto, HttpServletResponse response) {
        try {
            String msg = loginService.login(userDto);

            if ("ID 또는 PW가 일치하지 않습니다.".equals(msg)) {
                return ResponseEntity.badRequest().body(msg); // 400 상태코드 + 메시지 반환
            }

            User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            log.info("로그인한 사용자 정보: userId: {}, username: {}, name: {}, role: {}", user.getUserId(), user.getUsername(), user.getName(), user.getRole());

            // 인증 수행
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            // Access Token & Refresh Token 생성
            String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUsername(), user.getName(), user.getRole());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getUsername(), user.getName(), user.getRole());

            // Access Token을 HttpOnly Cookie에 저장
            jwtCookieUtil.addTokenToCookie(response, "accessSD", accessToken, 5); // 5초

            // Refresh Token을 HttpOnly Cookie에 저장 (세션)
            jwtCookieUtil.addTokenToCookie(response, "refreshSD", refreshToken, 0); // 세션 쿠키

            return ResponseEntity.ok()
                    .body(Map.of("message", "로그인 성공!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // HttpOnly Cookie 삭제
        jwtCookieUtil.deleteCookie(response, "accessSD");

        // HttpOnly Cookie 삭제
        jwtCookieUtil.deleteCookie(response, "refreshSD");

        return ResponseEntity.ok()
                .body(Map.of("message", "로그아웃 성공!"));
    }

    //ID 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        String msg = "이름과 이메일을 다시 확인해 주세요";
        UserDto user = loginService.findId(userDto);
        log.info("user: {}", user);
        if (user != null) {
            return ResponseEntity.ok("ID: " + user.getUsername());
        } else {
            return ResponseEntity.badRequest().body(msg);
        }
    }

    //PW 찾기
    @PostMapping("/find-pw")
    public ResponseEntity<?> findPw(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        String msg = "ID, 이름, 이메일을 다시 확인해 주세요";
        UserDto user = loginService.findPw(userDto);
        if (user != null) {
            return ResponseEntity.ok("find");
        } else {
            return ResponseEntity.badRequest().body(msg);
        }
    }

    //PW 변경
    @PostMapping("/change-pw")
    public ResponseEntity<?> changePw(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        int result = loginService.changePw(userDto);
        if (result == 1) {
            return ResponseEntity.ok("비밀번호 변경 완료!");
        } else {
            return ResponseEntity.badRequest().body("오류 발생. 다시 시도해 주세요.");
        }
    }

}

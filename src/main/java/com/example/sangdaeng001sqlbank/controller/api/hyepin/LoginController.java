package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.jwt.JwtTokenProvider;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import com.example.sangdaeng001sqlbank.service.hyepin.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final UserRepository userRepository;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> register(@ModelAttribute UserDto userDto) {
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute UserDto userDto) {
        try {
            String msg = loginService.login(userDto);

            if ("ID 또는 PW가 일치하지 않습니다.".equals(msg)) {
                return ResponseEntity.badRequest().body(msg); // 400 상태코드 + 메시지 반환
            }

            User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            log.info("로그인한 사용자 정보: username: {}, name: {}, role: {}", user.getUsername(), user.getName(), user.getRole());

            // 인증 수행
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getName(), user.getRole());

            // HttpOnly Cookie 설정
            ResponseCookie cookie = ResponseCookie.from("sangDaeng", token)
                    .httpOnly(true)   //  XSS 공격 방지 (JS에서 접근 불가)
                    .secure(true)     //  HTTPS에서만 전송 (개발 중에는 false)
                    .path("/")        //  모든 경로에서 접근 가능
                    .maxAge(3600)     //  1시간 유지
                    .sameSite("Strict") //  CSRF 방지
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("message", "로그인 성공!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // HttpOnly Cookie 삭제 (Set-Cookie로 빈 값 설정)
        ResponseCookie cookie = ResponseCookie.from("sangDaeng", "")
                .httpOnly(true)   // XSS 공격 방지 (JS에서 접근 불가)
                .secure(false)    // 개발 환경에서는 false, 배포 시 true (HTTPS 필요)
                .path("/")        // 모든 경로에서 접근 가능
                .maxAge(0)        // 즉시 만료 (쿠키 삭제)
                .sameSite("Strict") // CSRF 방지
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())  // 클라이언트에 쿠키 삭제 요청
                .body(Map.of("message", "로그아웃 성공!"));
    }
    
    //ID 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        String msg = "이름과 이메일을 다시 확인해 주세요";
        UserDto user = loginService.findId(userDto);
        log.info("user: {}", user);
        if(user != null) {
            return ResponseEntity.ok("ID: " + user.getUsername());
        }else{
            return ResponseEntity.badRequest().body(msg);
        }
    }

    //PW 찾기
    @PostMapping("/find-pw")
    public ResponseEntity<?> findPw(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        String msg = "ID, 이름, 이메일을 다시 확인해 주세요";
        UserDto user = loginService.findPw(userDto);
        if(user != null) {
            return ResponseEntity.ok("find");
        }else {
            return ResponseEntity.badRequest().body(msg);
        }
    }

    //PW 변경
    @PostMapping("/change-pw")
    public ResponseEntity<?> changePw(@ModelAttribute UserDto userDto) {
        log.info("userDto: {}", userDto);
        int result = loginService.changePw(userDto);
        if(result == 1) {
            return ResponseEntity.ok("비밀번호 변경 완료!");
        }else {
            return ResponseEntity.badRequest().body("오류 발생. 다시 시도해 주세요.");
        }
    }

}

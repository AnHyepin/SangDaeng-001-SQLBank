package com.example.sangdaeng001sqlbank.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieUtil {

    // Access Token & Refresh Token을 쿠키에 저장
    public void addTokenToCookie(HttpServletResponse response, String cookieName, String token, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가능

        // HTTPS가 아닐 때 Secure 속성 해제 (테스트 환경)
        boolean isLocal = isLocalEnvironment();
        cookie.setSecure(!isLocal); // HTTPS 환경에 맞게 조정
        cookie.setPath("/");

        // Refresh Token인 경우 세션 쿠키로 설정 (maxAge = -1)
        if (cookieName.equals("refreshSD")) {
            cookie.setMaxAge(-1);
        } else {
            // Access Token인 경우 5초로 설정
            cookie.setMaxAge(5);
        }

        response.addCookie(cookie);
    }

    // 특정 쿠키 삭제 (로그아웃 시 사용)
    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경에서는 true로 변경 가능
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료 (쿠키 삭제)
        response.addCookie(cookie);
    }

    // 현재 환경이 로컬인지 확인하는 메서드
    private boolean isLocalEnvironment() {
        String env = System.getProperty("spring.profiles.active");
        return env == null || env.equals("local");
    }
}
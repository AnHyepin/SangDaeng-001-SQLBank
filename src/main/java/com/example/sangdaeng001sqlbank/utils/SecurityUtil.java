package com.example.sangdaeng001sqlbank.utils;

import com.example.sangdaeng001sqlbank.jwt.JwtTokenProvider;
import com.example.sangdaeng001sqlbank.secu.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtTokenProvider jwtTokenProvider;

    // SecurityContextHolder에서 현재 로그인된 사용자 ID 가져오기
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    // SecurityContextHolder에서 현재 로그인된 사용자 이름 가져오기
    public static String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getName();
        }
        return null;
    }

    // SecurityContextHolder에서 현재 사용자 역할 가져오기
    public static String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                return authority.getAuthority();
            }
        }
        return "ROLE_UNKNOWN";
    }

    
    //JWT는 시험용으로 만든것. SecurityContextHolder 사용하시면 됩니다
    // JWT에서 ID 가져오기
    public String getUsernameFromToken(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getUsernameFromToken(token);
        }
        return null;
    }

    // JWT에서 이름 가져오기
    public String getNameFromToken(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getNameFromToken(token);
        }
        return null;
    }

    // JWT에서 Role 가져오기
    public String getRoleFromToken(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getRoleFromToken(token);
        }
        return "ROLE_UNKNOWN";
    }

    // 요청 헤더에서 JWT 토큰 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }



}

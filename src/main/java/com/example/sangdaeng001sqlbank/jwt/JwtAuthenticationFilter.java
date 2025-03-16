package com.example.sangdaeng001sqlbank.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.access-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private int refreshTokenExpiration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/hyepin/common/")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = getTokenFromCookie(request, "accessSD");

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            // Access Token이 유효하면 SecurityContext 설정
            setAuthentication(accessToken, request);
        } else {
            // Access Token이 만료되었으면 Refresh Token 확인
            String refreshToken = getTokenFromCookie(request, "refreshSD");

            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                int userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
                String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
                String name = jwtTokenProvider.getNameFromToken(refreshToken);
                String role = jwtTokenProvider.getRoleFromToken(refreshToken);

                // 새로운 Access Token & Refresh Token 발급
                String newAccessToken = jwtTokenProvider.createAccessToken(userId, username,name, role);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(userId, username, name, role);

                // TODO: 기존 Refresh Token 폐기
                //invalidateOldRefreshToken(refreshToken);

                // 새로운 Access Token & Refresh Token을 쿠키에 저장
                saveTokenToCookie(response, "accessSD", newAccessToken, accessTokenExpiration); // 1시간
                saveTokenToCookie(response, "refreshSD", newRefreshToken, refreshTokenExpiration); // 7일

                // 새 Access Token으로 SecurityContext 설정
                setAuthentication(newAccessToken, request);
            } else {
                // Refresh Token도 없으면 SecurityContext 초기화 (완전 로그아웃)
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    // SecurityContext 설정
    private void setAuthentication(String token, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.clearContext();
        }
    }

    // 특정 쿠키 값 가져오기
    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 새로운 JWT를 쿠키에 저장 (쿠키 갱신)
    private void saveTokenToCookie(HttpServletResponse response, String cookieName, String token, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 배포 시 true 설정 (HTTPS 필요)
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 기존 Refresh Token을 폐기 (보안 강화)
    private void invalidateOldRefreshToken(String refreshToken) {
        // TODO: Refresh Token을 블랙리스트에 추가하거나 DB에서 삭제하는 로직 (현재는 로직 추가 X)
        System.out.println("이전 Refresh Token 폐기: " + refreshToken);
    }
}
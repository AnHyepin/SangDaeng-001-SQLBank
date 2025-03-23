package com.example.sangdaeng001sqlbank.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final JwtCookieUtil jwtCookieUtil;

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
        log.info("Access Token: {}", accessToken);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            log.info("Access Token이 유효함");
            setAuthentication(accessToken, request);
        } else {
            log.info("Access Token이 만료되었거나 없음");
            String refreshToken = getTokenFromCookie(request, "refreshSD");
            log.info("Refresh Token: {}", refreshToken);

            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                log.info("Refresh Token이 유효함, 새로운 토큰 발급");
                int userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
                String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
                String name = jwtTokenProvider.getNameFromToken(refreshToken);
                String role = jwtTokenProvider.getRoleFromToken(refreshToken);

                // 새로운 Access Token & Refresh Token 발급
                String newAccessToken = jwtTokenProvider.createAccessToken(userId, username, name, role);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(userId, username, name, role);

                log.info("새로운 Access Token 발급: {}", newAccessToken);
                log.info("새로운 Refresh Token 발급: {}", newRefreshToken);

                // 새로운 토큰을 쿠키에 저장
                jwtCookieUtil.addTokenToCookie(response, "accessSD", newAccessToken, accessTokenExpiration); // 5초
                jwtCookieUtil.addTokenToCookie(response, "refreshSD", newRefreshToken, 0); // 세션 쿠키 (0으로 설정)

                // 새 Access Token으로 SecurityContext 설정
                setAuthentication(newAccessToken, request);
            } else {
                log.info("Refresh Token이 만료되었거나 없음");
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
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("SecurityContext에 인증 정보 설정 완료: {}", username);
        } else {
            log.info("UserDetails를 찾을 수 없음: {}", username);
            SecurityContextHolder.clearContext();
        }
    }

    // 특정 쿠키 값 가져오기
    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    log.info("쿠키 찾음 - {}: {}", cookieName, cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        log.info("쿠키를 찾을 수 없음: {}", cookieName);
        return null;
    }

    // 기존 Refresh Token을 폐기 (보안 강화)
    private void invalidateOldRefreshToken(String refreshToken) {
        // TODO: Refresh Token을 블랙리스트에 추가하거나 DB에서 삭제하는 로직 (현재는 로직 추가 X)
        System.out.println("이전 Refresh Token 폐기: " + refreshToken);
    }
}

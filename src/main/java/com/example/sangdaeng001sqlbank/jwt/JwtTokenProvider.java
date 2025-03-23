package com.example.sangdaeng001sqlbank.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.access-expiration}")
    private int accessTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Access Token 생성
    public String createAccessToken(int userId, String username, String name, String role) {
        log.info("Access Token 생성 - userId: {}, username: {}, name: {}, role: {}", userId, username, name, role);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("name", name)
                .claim("role", role)
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // 10분 후 만료
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성 (세션 유지)
    public String createRefreshToken(int userId, String username, String name, String role) {
        log.info("Refresh Token 생성 - userId: {}, username: {}, name: {}, role: {}", userId, username, name, role);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("name", name)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(null) // 세션 쿠키로 설정 (만료 시간 없음)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public int getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Number.class)
                .intValue();
    }

    public String getNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("name", String.class);
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            log.info("토큰 검증 성공");
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT 토큰이 만료됨: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("잘못된 JWT 형식: {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.error("JWT 서명이 올바르지 않음: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 비어 있음: {}", ex.getMessage());
        }
        return false;
    }
}

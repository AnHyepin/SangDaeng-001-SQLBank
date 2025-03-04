package com.example.sangdaeng001sqlbank.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 🔥 최신 버전에서는 key 자체를 사용
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("JWT 토큰이 만료됨: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("지원되지 않는 JWT 토큰: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("잘못된 JWT 형식: " + ex.getMessage());
        } catch (SignatureException ex) {
            System.out.println("JWT 서명이 올바르지 않음: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT 토큰이 비어 있음: " + ex.getMessage());
        }
        return false;
    }
}

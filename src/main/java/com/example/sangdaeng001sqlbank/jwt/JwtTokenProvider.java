package com.example.sangdaeng001sqlbank.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String createToken(String username, String name, String role) {
        log.info("JWT Claims username: {}, name: {}, role: {}", username, name, role);

        return Jwts.builder()
                .setSubject(username)
                .claim("name", name)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, key)
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

//    public String getNameFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("name", String.class);
//    }

    public String getNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("JWT Claims name: {}", claims);

        return claims.get("name", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("JWT Claims role: {}", claims);

        return claims.get("role", String.class);
    }

//    public String getRoleFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("role", String.class);
//    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
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

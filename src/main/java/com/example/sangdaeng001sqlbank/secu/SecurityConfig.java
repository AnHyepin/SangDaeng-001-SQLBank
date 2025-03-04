package com.example.sangdaeng001sqlbank.secu;

import com.example.sangdaeng001sqlbank.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/hyepin/common/**", "/sangin/common/**", "/layout/**").permitAll()
                        .requestMatchers("/hyepin/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/sangin/teacher/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/sangin/student/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                        .anyRequest().authenticated() //권한 없음
                        //.anyRequest().permitAll()
                );

        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




}

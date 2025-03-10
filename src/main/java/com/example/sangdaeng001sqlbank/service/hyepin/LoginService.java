package com.example.sangdaeng001sqlbank.service.hyepin;

import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.jwt.JwtTokenProvider;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //회원가입
    public String registerUser(UserDto userDto) {
        log.info("userDto: {}", userDto.toString());

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
           return "중복된 ID입니다.";
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "중복된 email입니다.";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRole("ROLE_STUDENT");
        userRepository.save(user);

        return "회원가입 완료!";
    }

    //로그인
    public String login(UserDto userDto) {
        // DB에서 사용자 정보 조회
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 인증 수행
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        // JWT 토큰 생성 후 반환
        return jwtTokenProvider.createToken(user.getUsername(), user.getName(), user.getRole());
    }

}

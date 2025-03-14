package com.example.sangdaeng001sqlbank.service.hyepin;

import com.example.sangdaeng001sqlbank.dao.hyepin.UserDao;
import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.jwt.JwtTokenProvider;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;

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
        log.info("userDto: {}", userDto.toString());

        // DB에서 사용자 조회 (없으면 null)
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);

        // PW 조회
        if (user == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return "ID 또는 PW가 일치하지 않습니다."; // 동일한 메시지 반환
        }

        return "로그인 성공!";
    }

    //ID 찾기
    public UserDto findId(UserDto userDto) {
        User user = userRepository.findByNameAndEmail(userDto.getName(), userDto.getEmail()).orElse(null);
        return (user != null) ?  UserDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build() : null;
    }

    //PW 찾기
    public UserDto findPw(UserDto userDto) {
        User user = userRepository.findByUsernameAndNameAndEmail(userDto.getUsername(), userDto.getName(), userDto.getEmail()).orElse(null);
        return (user != null) ?  UserDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build() : null;
    }

    public int changePw(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        int result  = userDao.updatePassword(userDto);
        return result;
    }
}
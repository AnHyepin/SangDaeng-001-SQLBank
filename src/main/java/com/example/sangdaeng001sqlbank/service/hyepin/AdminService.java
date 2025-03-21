package com.example.sangdaeng001sqlbank.service.hyepin;

import com.example.sangdaeng001sqlbank.dao.hyepin.AdminDao;
import com.example.sangdaeng001sqlbank.dto.AverageScore;
import com.example.sangdaeng001sqlbank.dto.CorrectDto;
import com.example.sangdaeng001sqlbank.dto.ScoreChangeDto;
import com.example.sangdaeng001sqlbank.dto.WrongRateDto;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final WebClient.Builder webClientBuilder;
    private final AdminDao adminDao;
    private final UserRepository userRepository;

    public Integer getClassNumber() {
        try {
            WebClient webClient = webClientBuilder.baseUrl("http://localhost:8080").build();

            return webClient.get()
                    .uri("/api/admin/class-num")
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
        } catch (Exception e) {
            return 1;
        }
    }

    public List<WrongRateDto> getWrongRateList(String difficulty) {
        return adminDao.getWrongRate(difficulty);
    }

    public List<CorrectDto> getCorrectRate(){
        return adminDao.getCorrectRate();
    }


    public User updateAuthority(User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new EntityNotFoundException());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    public List<AverageScore> getAverageScore() {
        return adminDao.getAverageScore();
    }

    public List<ScoreChangeDto> getScoreChange() {
        return adminDao.getScoreChange();
    }
}

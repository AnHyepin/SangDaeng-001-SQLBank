package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.RankingDao_sangin;
import com.example.sangdaeng001sqlbank.dto.RankingDto;
import com.example.sangdaeng001sqlbank.dto.RankingResponseDto;
import com.example.sangdaeng001sqlbank.dto.RankingData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService_sangin {

    private final RankingDao_sangin rankingDao;

    public RankingResponseDto getRankings() {
        log.info("랭킹 데이터 조회 서비스 시작");

        // 전체 참가자 수 조회
        int totalParticipants = rankingDao.getTotalParticipants();

        // 랭킹 데이터 조회
        List<RankingData> rankingDataList = rankingDao.getRankingData();

        // 엔티티를 DTO로 변환
        List<RankingDto> rankingDtos = rankingDataList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("랭킹 데이터 조회 완료: 총 {}개 랭킹", rankingDtos.size());

        // 응답 DTO 구성
        return RankingResponseDto.builder()
                .totalParticipants(totalParticipants)
                .rankings(rankingDtos)
                .build();
    }

    private RankingDto convertToDto(RankingData data) {
        return RankingDto.builder()
                .name(data.getUserName())
                .score(calculateScore(data))
                .correctRate(calculateCorrectRate(data))
                .attempts(data.getTotalAttempts())
                .difficulty(getDifficultyLevel(data))
                .build();
    }

    private int calculateScore(RankingData data) {
        // 점수 계산 로직 (예시)
        // 정답 수 × 10 + 시도 횟수 × 0.5 + 난이도 가중치
        int baseScore = data.getCorrectAnswers() * 10;
        double attemptBonus = data.getTotalAttempts() * 0.5;
        int difficultyBonus = getDifficultyBonus(data.getDifficulty());

        return (int) (baseScore + attemptBonus + difficultyBonus);
    }

    private int calculateCorrectRate(RankingData data) {
        if (data.getTotalAttempts() == 0) {
            return 0;
        }
        return (int) ((double) data.getCorrectAnswers() / data.getTotalAttempts() * 100);
    }

    private int getDifficultyBonus(String difficulty) {
        switch (difficulty) {
            case "EASY": return 1;
            case "MEDIUM": return 3;
            case "HARD": return 5;
            default: return 0;
        }
    }

    private String getDifficultyLevel(RankingData data) {
        // 사용자가 가장 많이 시도한 난이도를 반환
        if (data.getHardCount() > data.getMediumCount() && data.getHardCount() > data.getEasyCount()) {
            return "HARD";
        } else if (data.getMediumCount() > data.getEasyCount()) {
            return "MEDIUM";
        } else {
            return "EASY";
        }
    }
}
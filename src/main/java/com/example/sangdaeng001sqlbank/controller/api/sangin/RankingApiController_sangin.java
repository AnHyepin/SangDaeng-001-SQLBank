package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.RankingDto;
import com.example.sangdaeng001sqlbank.dto.RankingResponseDto;
import com.example.sangdaeng001sqlbank.service.sangin.RankingService_sangin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class RankingApiController_sangin {

    private final RankingService_sangin rankingService;

    @GetMapping("/rankings")
    public ResponseEntity<RankingResponseDto> getRankings() {
        log.info("랭킹 데이터 요청 수신");
        RankingResponseDto response = rankingService.getRankings();
        log.info("랭킹 데이터 응답: 전체 참가자 수 = {}, 랭킹 수 = {}",
                response.getTotalParticipants(),
                response.getRankings().size());
        return ResponseEntity.ok(response);
    }
}


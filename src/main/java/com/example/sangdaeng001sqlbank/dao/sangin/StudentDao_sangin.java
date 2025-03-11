package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentDao_sangin {
    List<ProblemDto> getProblemsByDifficulty(@Param("difficulty") String difficulty);

    List<ProblemChoiceDto> getProblemChoicesByDifficulty(@Param("problemId") int problemId);

    int getCorrectChoiceId(@Param("problemId") int problemId);

    /**
     * 풀이 회차 저장 후 session_id 반환
     **/
    void insertAttemptSession(AttemptSessionDto attemptSessionDto);

    /**
     * 사용자 풀이 기록 저장
     **/
    void insertAttemptDetail(AttemptDetailDto attemptDetailDto);

    void updateAttemptSessionScore(AttemptSessionDto attemptSessionDto);

    //사용자의 성적 리스트 조회
    List<ScoreListDto> getScoreList(@Param("userId") int userId);

    //특정 회차의 문제별 정답 상세 조회
    List<AttemptDetailDto> getAttemptDetails(@Param("sessionId") int sessionId);

    Integer getMaxAttemptTimes(int userId);

}

package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDiscussionDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonDao_sangin {
    // ✅ 문제 목록 조회 (페이징)
    List<ProblemDto> getProblemList(@Param("offset") int offset, @Param("size") int size);

    // ✅ 총 문제 개수 조회
    int getProblemCount();
    ProblemDto getProblemDetail(int problemId);

    // ✅ 댓글 삽입
    int addDiscussion(ProblemDiscussionDto problemDiscussionDto);

    // ✅ 특정 문제의 댓글 목록 조회
    List<ProblemDiscussionDto> getDiscussionsByProblemId(int problemId);



}

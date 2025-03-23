package com.example.sangdaeng001sqlbank.dao.hyepin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProblemDao {
    // 문제 목록 조회 (페이징, 검색 기능 포함)
    List<ProblemDto> getProblemList(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("type") String type,
            @Param("keyword") String keyword
    );

    // 총 문제 개수 조회 (검색 조건 포함)
    int getProblemCount(
            @Param("type") String type,
            @Param("keyword") String keyword
    );

    // 문제 상세 조회
    ProblemDto getProblemDetail(@Param("problemId") int problemId);
}
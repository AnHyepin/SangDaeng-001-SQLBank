package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemChoiceDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherDao_sangin {

    void insertProblem(ProblemDto problem);
    void insertProblemChoice(ProblemChoiceDto choice);

    List<ProblemDto> getProblemList();

    ProblemDto getProblemDetail(int problemId);
    List<ProblemChoiceDto> getProblemChoice(int problemId);

    int updateProblem(@Param("problemId") int problemId, @Param("problem") ProblemDto problem);
    // 기존 보기 삭제
    void deleteChoicesByProblemId(int problemId);

    int updateProblemStatus(@Param("problemId") int problemId, @Param("permitYn") String permitYn);

    int deleteProblem(int problemId);
}

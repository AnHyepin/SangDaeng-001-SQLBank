package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemChoiceDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherDao_sangin {

    void insertProblem(ProblemDto problem);

    // 문제 보기 저장
    void insertProblemChoice(ProblemChoiceDto choice);
}

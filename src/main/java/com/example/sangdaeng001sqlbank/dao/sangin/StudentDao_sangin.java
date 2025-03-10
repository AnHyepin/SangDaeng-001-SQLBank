package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemChoiceDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentDao_sangin {
    List<ProblemDto> getProblemsByDifficulty(@Param("difficulty") String difficulty);
    List<ProblemChoiceDto> getProblemChoicesByDifficulty(@Param("problemId") int problemId);
}

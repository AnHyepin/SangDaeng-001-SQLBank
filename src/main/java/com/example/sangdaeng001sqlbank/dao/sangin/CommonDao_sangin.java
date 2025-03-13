package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonDao_sangin {
    List<ProblemDto> getProblemList();

    ProblemDto getProblemDetail(int problemId);
}

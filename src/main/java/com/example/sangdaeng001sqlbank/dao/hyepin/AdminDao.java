package com.example.sangdaeng001sqlbank.dao.hyepin;

import com.example.sangdaeng001sqlbank.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDao {
    public List<WrongRateDto> getWrongRate(@Param("difficulty") String difficulty);
    public List<CorrectDto> getCorrectRate();
    public List<AverageScore> getAverageScore();
    public List<ScoreChangeDto> getScoreChange();
}

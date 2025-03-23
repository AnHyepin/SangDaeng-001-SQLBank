package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.RankingData;
import com.example.sangdaeng001sqlbank.dto.RankingDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RankingDao_sangin {
    int getTotalParticipants();

    List<RankingData> getRankingData();
}

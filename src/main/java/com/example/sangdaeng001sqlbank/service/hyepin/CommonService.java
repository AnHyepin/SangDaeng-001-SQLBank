package com.example.sangdaeng001sqlbank.service.hyepin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sangdaeng001sqlbank.dao.hyepin.ProblemDao;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;

@Service
@Transactional(readOnly = true)
public class CommonService {

    private final ProblemDao problemDao;

    public CommonService(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    // 문제 목록 조회 (페이징, 검색 기능 포함)
    public List<ProblemDto> getProblemList(int offset, int size, String type, String keyword) {
        return problemDao.getProblemList(offset, size, type, keyword);
    }

    // 총 문제 개수 조회 (검색 조건 포함)
    public int getProblemCount(String type, String keyword) {
        return problemDao.getProblemCount(type, keyword);
    }

    // 문제 상세 조회
    public ProblemDto getProblemDetail(int problemId) {
        return problemDao.getProblemDetail(problemId);
    }
}


package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.CommonDao_sangin;
import com.example.sangdaeng001sqlbank.dao.sangin.TeacherDao_sangin;
import com.example.sangdaeng001sqlbank.dto.ProblemDiscussionDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService_sangin {

    private final CommonDao_sangin commonDao;

    public CommonService_sangin(CommonDao_sangin commonDao) {
        this.commonDao = commonDao;
    }

    // ✅ 문제 목록 조회 (페이징 적용)
    public List<ProblemDto> getProblemList(int offset, int size) {
        return commonDao.getProblemList(offset, size);
    }

    // ✅ 총 문제 개수 조회
    public int getProblemCount() {
        return commonDao.getProblemCount();
    }

    public ProblemDto getProblemDetail(int problemId) {
        return commonDao.getProblemDetail(problemId);
    }

    public boolean addDiscussion(ProblemDiscussionDto problemDiscussionDto) {
        return commonDao.addDiscussion(problemDiscussionDto) > 0;
    }

    public List<ProblemDiscussionDto> getDiscussionsByProblemId(int problemId) {
        return commonDao.getDiscussionsByProblemId(problemId);
    }


}

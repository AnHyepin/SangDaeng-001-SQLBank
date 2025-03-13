package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.CommonDao_sangin;
import com.example.sangdaeng001sqlbank.dao.sangin.TeacherDao_sangin;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService_sangin {

    private final CommonDao_sangin commonDao;

    public CommonService_sangin(CommonDao_sangin commonDao) {
        this.commonDao = commonDao;
    }

    public List<ProblemDto> getProblemList() {
        return commonDao.getProblemList();
    }

    public ProblemDto getProblemDetail(int problemId) {
        return commonDao.getProblemDetail(problemId);
    }
}

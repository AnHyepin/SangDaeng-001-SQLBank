package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.TeacherDao_sangin;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.springframework.stereotype.Service;

@Service
public class TeacherService_sangin {

    private final TeacherDao_sangin teacherDao_sangin;

    public TeacherService_sangin(TeacherDao_sangin teacherDaoSangin) {
        teacherDao_sangin = teacherDaoSangin;
    }

    public void registProblem(ProblemDto requestDto) {
        problemDao.insertProblem(requestDto); // 문제정보 저장 (자동생성된 ID 반환필요)
        int problemId = requestDto.getProblemId();

        for (ProblemChoiceDto choice : requestDto.getChoices()) {
            choice.setProblemId(problemId);
            problemDao.insertProblemChoice(choice);
        }
    }

}

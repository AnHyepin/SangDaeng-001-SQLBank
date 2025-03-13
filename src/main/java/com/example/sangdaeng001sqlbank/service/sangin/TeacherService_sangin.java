package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.TeacherDao_sangin;
import com.example.sangdaeng001sqlbank.dto.ProblemChoiceDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService_sangin {

    private final TeacherDao_sangin teacherDao;

    public TeacherService_sangin(TeacherDao_sangin teacherDao) {
        this.teacherDao = teacherDao;
    }

    public void registProblem(ProblemDto requestDto) {
        teacherDao.insertProblem(requestDto); // 문제정보 저장 (자동생성된 ID 반환필요)

        int problemId = requestDto.getProblemId();

        for (ProblemChoiceDto choice : requestDto.getChoices()) {
            choice.setProblemId(problemId);
            teacherDao.insertProblemChoice(choice);
        }
    }



    public ProblemDto getProblemDetailLoadUpdate(int problemId) {
        ProblemDto problemDto = teacherDao.getProblemDetailLoadUpdate(problemId);
        List<ProblemChoiceDto> problemChoiceDtos = teacherDao.getProblemChoice(problemId);
        problemDto.setChoices(problemChoiceDtos);
        return problemDto;
    }

    @Transactional
    public boolean updateProblem(int problemId, ProblemDto requestDto) {
        // 문제 정보 업데이트
        int updatedRows = teacherDao.updateProblem(problemId, requestDto);
        if (updatedRows == 0) return false;

        // 기존 보기 삭제
        teacherDao.deleteChoicesByProblemId(problemId);

        // 새로운 보기 삽입
        for (ProblemChoiceDto choice : requestDto.getChoices()) {
            choice.setProblemId(problemId);
            teacherDao.insertProblemChoice(choice);
        }
        return true;
    }

    @Transactional
    public int updateProblemStatus(int problemId, String permitYn) {
        return teacherDao.updateProblemStatus(problemId, permitYn);
    }

    @Transactional
    public boolean deleteProblem(int problemId) {
        teacherDao.deleteChoicesByProblemId(problemId); // 보기도 삭제
        int deletedRows = teacherDao.deleteProblem(problemId);
        return deletedRows > 0;
    }

}

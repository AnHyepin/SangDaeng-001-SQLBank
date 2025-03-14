package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.*;
import com.example.sangdaeng001sqlbank.service.sangin.StudentService_sangin;
import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentApiController_sangin {
    private final StudentService_sangin studentService;

    public StudentApiController_sangin(StudentService_sangin studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/loadExam")
    public ResponseEntity<List<ProblemDto>> getProblemsByDifficulty(@RequestParam("difficulty") String difficulty) {
        List<ProblemDto> problems = studentService.getProblemsByDifficulty(difficulty);
        if (problems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(problems);
    }

    /** ✅ 시험 제출 API **/
    @PostMapping("/submitExam")
    public ResponseEntity<?> submitExam( @RequestBody ExamSubmissionDto answers) {
        if (answers == null) {
            return ResponseEntity.badRequest().body("문제가 제출되지 않았습니다.");
        }
        int userId = SecurityUtil.getUserId();
        int totalScore = studentService.submitExam(userId, answers);
        return ResponseEntity.ok().body("시험 제출 완료! 최종 점수: " + totalScore);
    }

    // ✅ 사용자의 성적 리스트 조회
    @GetMapping("/scoreList")
    public ResponseEntity<List<AttemptSessionDto>> getScoreList() {
        int userId = SecurityUtil.getUserId();
        List<AttemptSessionDto> scoreList = studentService.getScoreListWithDetails(userId);
        if (scoreList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(scoreList);
    }

    // ✅ 특정 회차의 상세 정답 조회
    @GetMapping("/scoreDetail")
    public ResponseEntity<List<AttemptDetailDto>> getAttemptDetails(@RequestParam("sessionId") int sessionId) {
        List<AttemptDetailDto> details = studentService.getAttemptDetails(sessionId);
        if (details.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(details);
    }

    @GetMapping("/problemDetail")
    public ResponseEntity<ProblemDetailDto> getProblemDetail(@RequestParam("problemId") int problemId) {
        ProblemDetailDto problemDetail = studentService.getProblemDetail(problemId);
        if (problemDetail == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(problemDetail);
    }


}

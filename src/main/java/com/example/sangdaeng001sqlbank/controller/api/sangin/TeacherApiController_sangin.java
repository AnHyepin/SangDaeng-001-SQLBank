package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.sangin.TeacherService_sangin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherApiController_sangin {

    private final TeacherService_sangin teacherService;

    public TeacherApiController_sangin(TeacherService_sangin teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registProblem(@RequestBody ProblemDto requestDto) {
        requestDto.setCreatedBy(1);
        if (requestDto == null || requestDto.getChoices() == null || requestDto.getChoices().isEmpty()) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        teacherService.registProblem(requestDto);  // DTO 전체 전달

        return ResponseEntity.ok().body("등록 성공");
    }

    @GetMapping("/problemList")
    public ResponseEntity<List<ProblemDto>> getProblemList() {
        if(teacherService.getProblemList() == null){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(teacherService.getProblemList());
    }

    @GetMapping("/problemDetail/{problemId}")
    public ResponseEntity<ProblemDto> getProblemDetail(@PathVariable int problemId) {
        if(teacherService.getProblemDetail(problemId) == null){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(teacherService.getProblemDetail(problemId));
    }

    @PutMapping("/update/{problemId}")
    public ResponseEntity<?> updateProblem(@PathVariable("problemId") int problemId, @RequestBody ProblemDto requestDto) {
        boolean updated = teacherService.updateProblem(problemId, requestDto);
        if (!updated) {
            return ResponseEntity.badRequest().body("문제 수정 실패");
        }
        return ResponseEntity.ok().body("문제 수정 성공");
    }

    // ✅ 문제 상태 변경 (활성화 <-> 비활성화) (PUT)
    @PutMapping("/updateStatus/{problemId}")
    public ResponseEntity<?> updateProblemStatus(@PathVariable("problemId") int problemId, @RequestBody Map<String, String> request) {
        String permitYn = request.get("permitYn");
        int updated = teacherService.updateProblemStatus(problemId, permitYn);
        if (updated == 0) {
            return ResponseEntity.badRequest().body("상태 변경 실패");
        }
        return ResponseEntity.ok().body("상태 변경 성공");
    }

    // ✅ 문제 삭제 (DELETE)
    @DeleteMapping("/delete/{problemId}")
    public ResponseEntity<?> deleteProblem(@PathVariable("problemId") int problemId) {
        boolean deleted = teacherService.deleteProblem(problemId);
        if (!deleted) {
            return ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok().body("삭제 성공");
    }


}

package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.sangin.StudentService_sangin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentApiController_sangin {
    private final StudentService_sangin studentService;

    public StudentApiController_sangin(StudentService_sangin studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/exam")
    public ResponseEntity<List<ProblemDto>> getProblemsByDifficulty(@RequestParam("difficulty") String difficulty) {
        List<ProblemDto> problems = studentService.getProblemsByDifficulty(difficulty);
        if (problems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("@@@@");
        System.out.println(problems);
        System.out.println("@@@@");
        return ResponseEntity.ok(problems);
    }
}

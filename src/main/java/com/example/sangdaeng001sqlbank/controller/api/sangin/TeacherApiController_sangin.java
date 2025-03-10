package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.sangin.TeacherService_sangin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherApiController_sangin {

    private final TeacherService_sangin teacherService;

    public TeacherApiController_sangin(TeacherService_sangin teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registProblem(@RequestBody ProblemDto requestDto) {
        requestDto.setCreatedBy("admin");
        System.out.println(requestDto);
        System.out.println(requestDto);
        System.out.println(requestDto);
        System.out.println(requestDto);
        if (requestDto == null || requestDto.getChoices() == null || requestDto.getChoices().isEmpty()) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        teacherService.registProblem(requestDto);  // DTO 전체 전달

        return ResponseEntity.ok().body("등록 성공");
    }


}

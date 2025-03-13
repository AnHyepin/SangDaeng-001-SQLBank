package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.sangin.CommonService_sangin;
import com.example.sangdaeng001sqlbank.service.sangin.TeacherService_sangin;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common")
public class CommonApiController_sangin {

    private final CommonService_sangin commonService;

    public CommonApiController_sangin(CommonService_sangin commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/problemList")
    public ResponseEntity<List<ProblemDto>> getProblemList() {
        if (commonService.getProblemList() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(commonService.getProblemList());
    }

    @GetMapping("/problemDetail/{problemId}")
    public ResponseEntity<ProblemDto> getProblemDetail(@PathVariable("problemId") int problemId) {
        if (commonService.getProblemDetail(problemId) == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(commonService.getProblemDetail(problemId));
    }

}

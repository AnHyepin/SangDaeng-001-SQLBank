package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import com.example.sangdaeng001sqlbank.entity.Problem;
import com.example.sangdaeng001sqlbank.service.hyepin.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/common/search")
@RequiredArgsConstructor
public class ProblemSearchController {

    private final ProblemService problemService;

    //
    @GetMapping("/problem-list/{type}/{keyword}")
    public ResponseEntity<List<Problem>> searchProblemList(
            @PathVariable String type,
            @PathVariable String keyword) {

        List<Problem> result = problemService.searchProblems(type, keyword);
        return ResponseEntity.ok(result);
    }

}

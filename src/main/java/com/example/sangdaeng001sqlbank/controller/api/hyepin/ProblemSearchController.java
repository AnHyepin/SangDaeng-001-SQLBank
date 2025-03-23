package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.hyepin.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common/search")
@RequiredArgsConstructor
public class ProblemSearchController {

    private final CommonService commonService;

//    @GetMapping("/problem-list/{type}/{keyword}")
//    public ResponseEntity<List<Problem>> searchProblemList(
//            @PathVariable String type,
//            @PathVariable String keyword) {
//
//        List<Problem> result = problemService.searchProblems(type, keyword);
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/problemList")
    public ResponseEntity<Map<String, Object>> getProblemList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword) {

        int offset = (page - 1) * size;
        List<ProblemDto> problems = commonService.getProblemList(offset, size, type, keyword);
        int totalCount = commonService.getProblemCount(type, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> response = new HashMap<>();
        response.put("problems", problems);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }


}

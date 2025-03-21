package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.ProblemDiscussionDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import com.example.sangdaeng001sqlbank.service.sangin.CommonService_sangin;
import com.example.sangdaeng001sqlbank.service.sangin.TeacherService_sangin;
import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
public class CommonApiController_sangin {

    private final CommonService_sangin commonService;

    public CommonApiController_sangin(CommonService_sangin commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/problemList")
    public ResponseEntity<Map<String, Object>> getProblemList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<ProblemDto> problems = commonService.getProblemList(offset, size);
        int totalCount = commonService.getProblemCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> response = new HashMap<>();
        response.put("problems", problems);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/problemDetail/{problemId}")
    public ResponseEntity<ProblemDto> getProblemDetail(@PathVariable("problemId") int problemId) {
        if (commonService.getProblemDetail(problemId) == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(commonService.getProblemDetail(problemId));
    }

    // ✅ 댓글 등록
    @PostMapping("/discussion")
    public ResponseEntity<String> addDiscussion(@RequestBody ProblemDiscussionDto problemDiscussionDto) {
        int userId = SecurityUtil.getUserId();
        problemDiscussionDto.setUserId(userId);
        boolean isSuccess = commonService.addDiscussion(problemDiscussionDto);
        if (isSuccess) {
            return ResponseEntity.ok("댓글이 등록되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("댓글 등록 실패");
        }
    }

    // ✅ 특정 문제에 대한 댓글 목록 조회
    @GetMapping("/discussionList")
    public ResponseEntity<List<ProblemDiscussionDto>> discussionList(@RequestParam("problemId") int problemId) {
        List<ProblemDiscussionDto> discussions = commonService.getDiscussionsByProblemId(problemId);
        return ResponseEntity.ok(discussions);
    }


}

package com.example.sangdaeng001sqlbank.controller.api.sangin;

import com.example.sangdaeng001sqlbank.dto.CommentDto;
import com.example.sangdaeng001sqlbank.dto.PostDto;
import com.example.sangdaeng001sqlbank.service.sangin.BoardService_sangin;
import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.apache.ibatis.annotations.Param;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
public class BoardApiController_sangin {

    private final BoardService_sangin boardService;

    public BoardApiController_sangin(BoardService_sangin boardService) {
        this.boardService = boardService;
    }

    // ✅ 게시글 등록
    @PostMapping("/post")
    public ResponseEntity<Integer> post(@RequestBody PostDto postDto) {
        if (postDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int userId = SecurityUtil.getUserId();
        postDto.setUserId(userId);
        return ResponseEntity.ok(boardService.addPost(postDto));
    }

    // ✅ 게시글 목록 조회


    @GetMapping("/postList")
    public Map<String, Object> getPostList(@RequestParam("category") String category,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<PostDto> posts = boardService.getPostList(category, offset, size);
        int totalCount = boardService.getPostCount(category);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);
        return result;
    }


    @GetMapping("/noticeList")
    public List<PostDto> getNoticeList() {
        return boardService.getRecentNoticeList();
    }


    // ✅ 게시글 상세 조회
    @GetMapping("/postDetail/{postId}")
    public ResponseEntity<PostDto> getPostDetail(@PathVariable("postId") int postId) {
        PostDto post = boardService.getPostDetail(postId);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    // ✅ 게시글 수정
    @PutMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") int postId, @RequestBody PostDto postDto) {
        if (postDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int userId = SecurityUtil.getUserId();
        postDto.setUserId(userId);
        postDto.setPostId(postId);
        if (boardService.updatePost(postDto) > 0) {
            return ResponseEntity.ok("게시글 수정에 성공하였습니다.");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ✅ 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") int postId) {
        boolean isDeleted = boardService.deletePost(postId);
        return isDeleted ? ResponseEntity.ok("게시글이 삭제되었습니다.") : ResponseEntity.badRequest().body("삭제 실패");
    }

    // ✅ 댓글 등록
    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto) {
        int userId = SecurityUtil.getUserId();
        commentDto.setUserId(userId);
        int result = boardService.addComment(commentDto);
        if (result > 0) {
            return ResponseEntity.ok("✅ 댓글이 등록되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("🚨 댓글 등록에 실패했습니다.");
        }
    }

    // ✅ 특정 게시글의 댓글 목록 조회
    @GetMapping("/commentList")
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam("postId") int postId) {
        List<CommentDto> comments = boardService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    // ✅ 댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable int commentId, @RequestBody Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("내용이 비어있습니다.");
        }
        boardService.updateComment(commentId, content.trim());
        return ResponseEntity.ok().build();
    }


    // ✅ 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") int commentId) {
        int result = boardService.deleteComment(commentId);
        if (result > 0) {
            return ResponseEntity.ok("✅ 댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("🚨 댓글 삭제에 실패했습니다.");
        }
    }
}

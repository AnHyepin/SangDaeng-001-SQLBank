package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.BoardDao_sangin;
import com.example.sangdaeng001sqlbank.dto.CommentDto;
import com.example.sangdaeng001sqlbank.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService_sangin {

    private final BoardDao_sangin boardDao;

    public BoardService_sangin(BoardDao_sangin boardDao) {
        this.boardDao = boardDao;
    }
    // ✅ 게시글 목록 조회
    public int addPost(PostDto postDto) {
        boardDao.addPost(postDto);
        return postDto.getPostId();
    }

    // ✅ 게시글 목록 조회
    public List<PostDto> getPostList(String category) {
        return boardDao.getPostList(category);
    }

    // ✅ 게시글 상세 조회
    public PostDto getPostDetail(int postId) {
        return boardDao.getPostDetail(postId);
    }
    // ✅ 게시글 수정
    public int updatePost(PostDto postDto) {
        return boardDao.updatePost(postDto);
    }

    // ✅ 게시글 삭제
    public boolean deletePost(int postId) {
        return boardDao.deletePost(postId) > 0;
    }
    // ✅ 댓글 등록
    public int addComment(CommentDto commentDto) {
        return boardDao.addComment(commentDto);
    }

    // ✅ 특정 게시글의 댓글 목록 조회
    public List<CommentDto> getCommentsByPostId(int postId) {
        return boardDao.getCommentsByPostId(postId);
    }

    // ✅ 댓글 삭제
    public int deleteComment(int commentId) {
        return boardDao.deleteComment(commentId);
    }
}

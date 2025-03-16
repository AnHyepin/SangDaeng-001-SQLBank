package com.example.sangdaeng001sqlbank.dao.sangin;

import com.example.sangdaeng001sqlbank.dto.CommentDto;
import com.example.sangdaeng001sqlbank.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardDao_sangin {
    // ✅ 게시글 등록
    void addPost(PostDto postDto);

    // ✅ 게시글 목록 조회
    List<PostDto> getPostList(String category);

    // ✅ 게시글 상세 조회
    PostDto getPostDetail(int postId);

    // ✅ 게시글 수정
    int updatePost(PostDto postDto);

    // ✅ 게시글 삭제
    int deletePost(int postId);

    // ✅ 댓글 등록
    int addComment(CommentDto commentDto);

    // ✅ 특정 게시글의 댓글 목록 조회
    List<CommentDto> getCommentsByPostId(int postId);

    // ✅ 댓글 삭제
    int deleteComment(int commentId);
}

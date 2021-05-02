package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.CommentRequestDto;
import com.demoim_backend.demoim_backend.dto.CommentResponseDto;
import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //SmallTalk 댓글 작성
    @PostMapping("/api/smalltalk/comments")
    public CommentResponseDto createCommentForSmallTalk(Authentication authentication, @RequestBody CommentRequestDto commentRequestDto,
                                                        @RequestParam(value ="smalltalk_id") Long smallTalkId){
        return commentService.createCommentForSmallTalk(authentication, commentRequestDto, smallTalkId);
    }

    //SmallTalk 댓글 조회
    @GetMapping("/api/smalltalk/comments")
    public List<CommentResponseDto> getCommentForSmallTalk(@RequestParam(value = "smalltalk_id") Long smallTalkId){
        return commentService.getCommentForSmallTalk(smallTalkId);
    }


    //Exhibition 댓글 작성
    @PostMapping("/api/exhibition/comments")
    public CommentResponseDto createCommentForExhibition(Authentication authentication, @RequestBody CommentRequestDto commentRequestDto,
                                                        @RequestParam(value ="exhibition_id") Long exhibitionId){
        return commentService.createCommentForExhibition(authentication, commentRequestDto, exhibitionId);
    }

    // Exhibition 댓글 조회
    @GetMapping("/api/exhibition/comments")
    public List<CommentResponseDto> getCommentForExhibition(@RequestParam(value = "exhibition_id") Long exhibitionId){
        return commentService.getCommentForExhibition(exhibitionId);
    }

    //SmallTalk & Exhibition 댓글 수정
    @PutMapping("/api/update/comments")
    public CommentResponseDto updateComment(Authentication authentication, @RequestBody CommentRequestDto commentRequestDto,
                                                        @RequestParam(value = "comment_id") Long id){
        return commentService.updateComment(authentication,commentRequestDto, id);
    }

    //SmallTalk & Exhibition 댓글 삭제
    @DeleteMapping("/api/delete/comments")
    public Map<String, String> deleteComment(Authentication authentication,
                                                         @RequestParam(value = "comment_id") Long id){
        return commentService.deleteComment(authentication, id);
    }
}

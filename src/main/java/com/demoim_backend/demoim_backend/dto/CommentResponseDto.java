package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String comments;
    private Long userid;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comments = comment.getComments();
        this.userid = comment.getCommentUser().getId();
        this.nickname = comment.getCommentUser().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}

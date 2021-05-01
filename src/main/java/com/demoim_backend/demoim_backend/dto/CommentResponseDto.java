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
    private ResponseUser user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comment, ResponseUser responseUser) {
        this.id = comment.getId();
        this.comments = comment.getComments();
        this.user = responseUser;
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}

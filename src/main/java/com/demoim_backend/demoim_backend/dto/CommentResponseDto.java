package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String comments;
    private ResponseUserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comment, ResponseUserDto responseUserDto) {
        this.id = comment.getId();
        this.comments = comment.getComments();
        this.user = responseUserDto;
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}

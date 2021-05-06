package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SmallTalkResponseDto {
    private Long id;
    private String contents;
    private ResponseUser user;
    private List<CommentResponseDto> commentList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public SmallTalkResponseDto(Long id, String contents, ResponseUser user, List<CommentResponseDto> commentList,  LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.commentList = commentList;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

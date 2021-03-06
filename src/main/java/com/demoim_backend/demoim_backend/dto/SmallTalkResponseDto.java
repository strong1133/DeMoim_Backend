package com.demoim_backend.demoim_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SmallTalkResponseDto {
    private Long smallTalkId;
    private String contents;
    private ResponseUserDto user;
    private List<CommentResponseDto> commentList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public SmallTalkResponseDto(Long id, String contents, ResponseUserDto user, List<CommentResponseDto> commentList, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.smallTalkId = id;
        this.contents = contents;
        this.user = user;
        this.commentList = commentList;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

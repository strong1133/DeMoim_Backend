package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class SmallTalkResponseDto {
    private Long id;
    private String content;
    private Long userid;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public SmallTalkResponseDto(Long id, String content, User user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.userid = user.getId();
        this.username = user.getUsername();
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

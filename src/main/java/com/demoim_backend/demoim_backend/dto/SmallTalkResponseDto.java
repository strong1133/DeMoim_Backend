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
    private String contents;
    private Long userid;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public SmallTalkResponseDto(Long id, String contents, User user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.contents = contents;
        this.userid = user.getId();
        this.nickname = user.getNickname();
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

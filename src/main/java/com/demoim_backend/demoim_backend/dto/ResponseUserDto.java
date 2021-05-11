package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseUserDto {

    private Long userId;
    private String username;
    private String nickname;
    private String position;
    private String description;
    private String profileImage;

    @Builder
    public ResponseUserDto(Long userId, String username, String nickname, String position, String description, String profileImage ) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.position = position;
        this.description= description;
        this.profileImage = profileImage;
    }

    public ResponseUserDto entityToDto(User user){
        return  ResponseUserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .position(user.getPosition())
                .description(user.getDescription())
                .profileImage(user.getProfileImage())
                .build();
    }

}
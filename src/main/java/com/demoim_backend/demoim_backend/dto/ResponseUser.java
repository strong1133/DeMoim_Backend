package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseUser {

    private Long userid;
    private String username;
    private String nickname;
    private String profileImage;

    @Builder
    public ResponseUser(Long userid, String username, String nickname, String profileImage ) {
        this.userid = userid;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public ResponseUser entityToDto(User user){
        return  ResponseUser.builder()
                .userid(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

}
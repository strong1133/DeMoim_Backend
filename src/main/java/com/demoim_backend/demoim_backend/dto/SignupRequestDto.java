package com.demoim_backend.demoim_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String position;

    public SignupRequestDto(String username, String password, String nickname, String position) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.position = position;
    }

}

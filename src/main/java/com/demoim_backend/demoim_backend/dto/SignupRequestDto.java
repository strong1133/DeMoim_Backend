package com.demoim_backend.demoim_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String position;

}

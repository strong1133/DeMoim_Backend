package com.demoim_backend.demoim_backend.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
// 로그인을 위한 DTO
public class LoginRequestDto {
    private String username;
    private String password;
}

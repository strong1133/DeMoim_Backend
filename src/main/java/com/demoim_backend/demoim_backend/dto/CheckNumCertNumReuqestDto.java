package com.demoim_backend.demoim_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CheckNumCertNumReuqestDto {

    @NotBlank(message = "아이디를 입력하세요.")
    @Email(message = "아이디는 이메일 형식이여야합니다.")
    private String username;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String certNumber;

}
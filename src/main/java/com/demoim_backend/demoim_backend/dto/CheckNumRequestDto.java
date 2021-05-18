package com.demoim_backend.demoim_backend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CheckNumRequestDto {

    @NotBlank(message = "번호를 입력하세요.")
    private String phoneNum;
}

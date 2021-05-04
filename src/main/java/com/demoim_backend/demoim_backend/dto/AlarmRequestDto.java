package com.demoim_backend.demoim_backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class AlarmRequestDto {

    private Long userId;
    private String contents;
}

package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Exhibition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
@Setter
public class ExhibitionDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String contents;

    private String thumbnail;


    public Exhibition toEntity(){
        return Exhibition.builder()
                .title(title)
                .contents(contents)
                .thumbnail(thumbnail)
                .build();
    }
}
package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.SmallTalk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class SmallTalkDto {

    @NotBlank
    private String content;

    public SmallTalk toEntity(){
        return SmallTalk.builder()
                .contents(content)
                .build();
    }
}

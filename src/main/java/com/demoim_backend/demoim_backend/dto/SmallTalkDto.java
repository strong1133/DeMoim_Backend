package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.SmallTalk;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class SmallTalkDto {

    @NotBlank(message = "내용을 입력하세요.")
    private String contents;


    @Builder
    public SmallTalkDto(String contents) {
        this.contents = contents;
    }

    public SmallTalk toEntity(){
        return SmallTalk.builder()
                .contents(contents)
                .build();
    }
}

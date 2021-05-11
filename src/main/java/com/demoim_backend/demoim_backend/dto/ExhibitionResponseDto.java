package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Exhibition;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ExhibitionResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String thumbnail;
    private ResponseUserDto user;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ExhibitionResponseDto(Long id, String title, String contents, String thumbnail, ResponseUserDto user, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.thumbnail = thumbnail;
        this.user = user;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    public ExhibitionResponseDto entityToDto(Exhibition exhibition){
        ResponseUserDto responseUserDto = new ResponseUserDto();

        return ExhibitionResponseDto.builder()
                .id(exhibition.getId())
                .title(exhibition.getTitle())
                .contents(exhibition.getContents())
                .user(responseUserDto.entityToDto(exhibition.getExhibitionUser()))
                .thumbnail(exhibition.getThumbnail())
                .createAt(exhibition.getCreatedAt())
                .modifiedAt(exhibition.getModifiedAt())
                .build();

    }
}

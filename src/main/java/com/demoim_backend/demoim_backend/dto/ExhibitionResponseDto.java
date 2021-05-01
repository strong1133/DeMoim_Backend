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
    private Long userid;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ExhibitionResponseDto(Long id, String title, String contents, String thumbnail, Long userid, String username, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.thumbnail = thumbnail;
        this.userid = userid;
        this.username = username;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    public ExhibitionResponseDto entityToDto(Exhibition exhibition){
        return ExhibitionResponseDto.builder()
                .id(exhibition.getId())
                .title(exhibition.getTitle())
                .contents(exhibition.getContents())
                .userid(exhibition.getExhibitionUser().getId())
                .username(exhibition.getExhibitionUser().getUsername())
                .thumbnail(exhibition.getThumbnail())
                .createAt(exhibition.getCreatedAt())
                .modifiedAt(exhibition.getModifiedAt())
                .build();

    }
}

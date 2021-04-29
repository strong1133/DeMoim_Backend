package com.demoim_backend.demoim_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
//@Builder
@NoArgsConstructor
public class TeamRequestDto {
    private String title;
    private LocalDateTime recruit;
    private LocalDateTime begin;
    private LocalDateTime end;
    private String location;
    private String thumbnail;
    private int front;
    private int back;
    private int designer;
    private int planner;
    private String stack;
    private String contents;

}


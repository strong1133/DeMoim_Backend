package com.demoim_backend.demoim_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
//@Builder
@NoArgsConstructor
public class TeamRequestDto {
    private String title;
//    private Date recruit;
//    private Date begin;
//    private Date end;
    private Date recruit;
    private Date begin;
    private Date end;
    private String location;
    private String thumbnail;
    private int front;
    private int back;
    private int designer;
    private int planner;
    private String stack;
    private String contents;

    @Builder
    public TeamRequestDto(String title, Date recruit, Date begin, Date end, String location, String thumbnail, int front, int back, int designer, int planner, String stack, String contents) {
        this.title = title;
        this.recruit = recruit;
        this.begin = begin;
        this.end = end;
        this.location = location;
        this.thumbnail = thumbnail;
        this.front = front;
        this.back = back;
        this.designer = designer;
        this.planner = planner;
        this.stack = stack;
        this.contents = contents;
    }
}


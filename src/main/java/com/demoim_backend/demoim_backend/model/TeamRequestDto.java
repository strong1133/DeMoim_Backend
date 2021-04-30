package com.demoim_backend.demoim_backend.model;

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

}


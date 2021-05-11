package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.List;

@NoArgsConstructor
@Getter
public class ActiveTeamResponseDto {
    private Long teamId;
    private String title;
    private Long createdAt;
    private Long recruit;
    private Long begin;
    private Long end;
    private String location;
    private String thumbnail;
    private int front;
    private int back;
    private int designer;
    private int planner;
    private String stack;
    private String contents;
    private Team.StateNow recruitState;
    private Team.StateNow projectState;

    List<User> member;

    @Builder
    public ActiveTeamResponseDto(Team team, List<User> member) {
        this.teamId = team.getId();
        this.title = team.getTitle();
        this.createdAt = team.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.recruit = team.getRecruit().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.begin = team.getBegin().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.end = team.getEnd().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.location = team.getLocation();
        this.thumbnail = team.getThumbnail();
        this.front = team.getFront();
        this.back = team.getBack();
        this.designer = team.getDesigner();
        this.planner = team.getPlanner();
        this.stack = team.getStack();
        this.contents = team.getContents();
        this.recruitState = team.getRecruitState();
        this.projectState = team.getProjectState();
        this.member = member;

    }


}

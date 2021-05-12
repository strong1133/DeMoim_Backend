package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponseDto {
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
    private UserUpdateProfileSaveRequestDto leader;

    @Builder
    public TeamResponseDto(TeamRequestDto teamRequestDto, UserUpdateProfileSaveRequestDto leaderProfile) {
        this.title = teamRequestDto.getTitle();
        this.recruit = teamRequestDto.getRecruit();
        this.begin = teamRequestDto.getBegin();
        this.end = teamRequestDto.getEnd();
        this.location = teamRequestDto.getLocation();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
        this.recruitState = Team.StateNow.ACTIVATED;
        this.projectState = Team.StateNow.YET;
        this.leader = leaderProfile;
    }

    @Builder
    public TeamResponseDto(Team team, UserUpdateProfileSaveRequestDto leaderProfile) {
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
        this.leader = leaderProfile;
    }

//    @Builder
//    public TeamResponseDto(String title, Date recruit, Date begin, Date end, String location, String thumbnail, int front, int back, int designer, int planner, String stack, String contents, UserUpdateProfileSaveDto leaderProfile) {
//        this.title = title;
//        this.recruit = recruit;
//        this.begin = begin;
//        this.end = end;
//        this.location = location;
//        this.thumbnail = thumbnail;
//        this.front = front;
//        this.back = back;
//        this.designer = designer;
//        this.planner = planner;
//        this.stack = stack;
//        this.contents = contents;
//        this.leader = leaderProfile;
//    }
}

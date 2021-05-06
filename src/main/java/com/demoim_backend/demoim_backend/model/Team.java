package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.dto.TeamStateUpdateResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Team extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id @Column(name = "teamId")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true, length = 400)
    private String thumbnail;

    //    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date recruit;
    private LocalDateTime recruit;

    //    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date begin;
    private LocalDateTime begin;

    //    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date end;
    private LocalDateTime end;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private int front;

    @Column(nullable = true)
    private int back;

    @Column(nullable = true)
    private int designer;

    @Column(nullable = true)
    private int planner;

    @Column(nullable = false)
    private String stack; // 선호언어

    @Column(nullable = false)
    private String contents;

//    @Column(nullable = false)
//    @JoinColumn(name = "userId") // 이걸로 해결할 수 있는 방법이 없을까..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User leader;

    /*
    ElementCollection의 형태 or List<String? Long?>의 형태로 받아서 0번째 index를 리더로 생각한다면 어떨까?
    이렇게 생각하지 않는다면 User Table로부터 ManyToOne 방식으로 Mappedby(name="project")가 되지 않을까?
     */
//    @Column(nullable = false)
//    private Long leaderId; // 리더 유저Id 값이 담김
    @OneToMany(mappedBy ="team", cascade = CascadeType.ALL)
    private List<TeamUserInfo> teamUserInfoList = new ArrayList<TeamUserInfo>();

    @Enumerated(EnumType.STRING)
    private StateNow recruitState; // 여기서 초기화를 하면 업데이트 불가능

    @Enumerated(EnumType.STRING)
    private StateNow projectState;

    public enum StateNow {
        YET, ACTIVATED, FINISHED
    }

    //연관관계 메소드 Team <-> TeamUserInfo
    public void addTeamUserInfo(TeamUserInfo teamUserInfo) {
        teamUserInfoList.add(teamUserInfo);
        teamUserInfo.setTeam(this);
    }


    public Team(TeamRequestDto teamRequestDto, User user) {
        this.title = teamRequestDto.getTitle();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.recruit = Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.begin = Instant.ofEpochMilli(teamRequestDto.getBegin()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.end = Instant.ofEpochMilli(teamRequestDto.getEnd()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.location = teamRequestDto.getLocation();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
        this.recruitState = StateNow.ACTIVATED;
        this.projectState = StateNow.YET;
        this.leader = user;
    }

    public void update(TeamRequestDto teamRequestDto) {
        this.title = teamRequestDto.getTitle();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.recruit = Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.begin = Instant.ofEpochMilli(teamRequestDto.getBegin()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.end = Instant.ofEpochMilli(teamRequestDto.getEnd()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.location = teamRequestDto.getLocation();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
    }

    //생성메소드 User 정보는 from Authentication, 나머지는 from TeamRequestDto
    public static Team createTeam(TeamRequestDto teamRequestDto, User user) {
        Team team = new Team();
        team.setTitle(teamRequestDto.getTitle());
        team.setThumbnail(teamRequestDto.getThumbnail());
        team.setRecruit(Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        team.setBegin(Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        team.setEnd(Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        team.setLocation(teamRequestDto.getLocation());
        team.setFront(teamRequestDto.getFront());
        team.setBack(teamRequestDto.getBack());
        team.setDesigner(teamRequestDto.getDesigner());
        team.setPlanner(teamRequestDto.getPlanner());
        team.setStack(teamRequestDto.getStack());
        team.setContents(teamRequestDto.getContents());
        team.setRecruitState(StateNow.ACTIVATED);
        team.setProjectState(StateNow.YET);
        team.setLeader(user);
        return team;
    }

    public void updateByTeamStateUpdateResponseDto(TeamStateUpdateResponseDto teamStateUpdateResponseDto) {
        this.recruitState = teamStateUpdateResponseDto.getRecruitState();
        this.projectState = teamStateUpdateResponseDto.getProjectState();
    }
}

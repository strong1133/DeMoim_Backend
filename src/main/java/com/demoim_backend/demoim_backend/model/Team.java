package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.dto.TeamStateUpdateResponseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.soap.Text;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @Column(nullable = true, length = 600)
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

    @Column(nullable = false,  length = 3000)
    private String contents;
//    @Column(columnDefinition = "TEXT", nullable = false,  length = 1000)

    @Column(nullable = false)
    private Boolean isBackFull;

    @Column(nullable = false)
    private Boolean isFrontFull;

    @Column(nullable = false)
    private Boolean isDesignerFull;

    @Column(nullable = false)
    private Boolean isPlannerFull;

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
    @JsonIgnore
    @OneToMany(mappedBy ="team", cascade = CascadeType.ALL)
    private List<ApplyInfo> applyInfoList = new ArrayList<ApplyInfo>();

    @Enumerated(EnumType.STRING)
    private StateNow recruitState; // 여기서 초기화를 하면 업데이트 불가능

    @Enumerated(EnumType.STRING)
    private StateNow projectState;

    public enum StateNow {
        YET, ACTIVATED, FINISHED
    }

    //연관관계 메소드 Team <-> ApplyInfo
    public void addTeamUserInfo(ApplyInfo applyInfo) {
        applyInfoList.add(applyInfo);
        applyInfo.setTeam(this);
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
        this.isFrontFull = teamRequestDto.getFront() == 0;
        this.isBackFull = teamRequestDto.getBack() == 0;
        this.isDesignerFull = teamRequestDto.getDesigner() == 0;
        this.isPlannerFull = teamRequestDto.getPlanner() == 0;
        this.recruitState = StateNow.ACTIVATED;
        this.projectState = StateNow.YET;
        this.leader = user;
    }

    //모집기간은 수정못하고 프로젝트기간만 수정할 수 있게끔!
    public void update(TeamRequestDto teamRequestDto) {
        this.title = teamRequestDto.getTitle();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.recruit = Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime(); //그럼 얘를 수정못하게 해줘야할텐데..?
        this.begin = Instant.ofEpochMilli(teamRequestDto.getBegin()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.end = Instant.ofEpochMilli(teamRequestDto.getEnd()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.location = teamRequestDto.getLocation();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
        //다음 네가지에 대해서는 position별 숫자가 줄어드는 경우에 추가적인 로직을 통한 보완이 필요하다. 아마 Service쪽에서 조건문 통해 구현하는 방법도 있을 듯
        this.isFrontFull = teamRequestDto.getFront() == 0;
        this.isBackFull = teamRequestDto.getBack() == 0;
        this.isDesignerFull = teamRequestDto.getDesigner() == 0;
        this.isPlannerFull = teamRequestDto.getPlanner() == 0;
    }

    //생성메소드 User 정보는 from Authentication, 나머지는 from TeamRequestDto
    public static Team createTeam(TeamRequestDto teamRequestDto, User user) {
        Team team = new Team();
        team.setTitle(teamRequestDto.getTitle());
        team.setThumbnail(teamRequestDto.getThumbnail());
        team.setRecruit(Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());

        team.setBegin(Instant.ofEpochMilli(teamRequestDto.getBegin()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        team.setEnd(Instant.ofEpochMilli(teamRequestDto.getEnd()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());

        team.setLocation(teamRequestDto.getLocation());
        team.setFront(teamRequestDto.getFront());
        team.setBack(teamRequestDto.getBack());
        team.setDesigner(teamRequestDto.getDesigner());
        team.setPlanner(teamRequestDto.getPlanner());
        team.setStack(teamRequestDto.getStack());
        team.setContents(teamRequestDto.getContents());
        team.setIsFrontFull(teamRequestDto.getFront() == 0);
        team.setIsBackFull(teamRequestDto.getBack() == 0);
        team.setIsDesignerFull(teamRequestDto.getDesigner() == 0);
        team.setIsPlannerFull(teamRequestDto.getPlanner() == 0);
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

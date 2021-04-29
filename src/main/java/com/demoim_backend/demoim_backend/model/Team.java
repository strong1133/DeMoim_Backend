package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Team extends Timestamped {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
  
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime recruit;

    @Column(nullable = false)
    private LocalDateTime begin;

    @Column(nullable = false)
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

    /*
    ElementCollection의 형태 or List<String? Long?>의 형태로 받아서 0번째 index를 리더로 생각한다면 어떨까?
    이렇게 생각하지 않는다면 User Table로부터 ManyToOne 방식으로 Mappedby(name="project")가 되지 않을까?
     */
//    @Column(nullable = false)
//    private Long leaderId; // 리더 유저Id 값이 담김
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user; // 리더에 대한 정보


    public Team(TeamRequestDto teamRequestDto) {
        this.title = teamRequestDto.getTitle();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.recruit = teamRequestDto.getRecruit();
        this.begin = teamRequestDto.getBegin();
        this.end = teamRequestDto.getEnd();
        this.location = teamRequestDto.getLocation();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
    }

    public void update(TeamRequestDto teamRequestDto) {
        this.title = teamRequestDto.getTitle();
        this.thumbnail = teamRequestDto.getThumbnail();
        this.recruit = teamRequestDto.getRecruit();
        this.begin = teamRequestDto.getBegin();
        this.end = teamRequestDto.getEnd();
        this.location = teamRequestDto.getLocation();
        this.front = teamRequestDto.getFront();
        this.back = teamRequestDto.getBack();
        this.designer = teamRequestDto.getDesigner();
        this.planner = teamRequestDto.getPlanner();
        this.stack = teamRequestDto.getStack();
        this.contents = teamRequestDto.getContents();
    }
}

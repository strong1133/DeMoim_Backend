package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class TeamUserInfo {

    /*
    가지고 있어야할 element목록
    id(PK), userId(FK), teamId(FK), leader, member, isAccepted
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "teamUserInfo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @Column
    private Boolean isAccepted;

    @Column
    private String message;

    @Column
    private String portfolio;


    public enum Membership {
        LEADER, MEMBER //Member라는 element 피하는게 좋을수도.. -> 대문자 처리
    }

    public static TeamUserInfo createTeamUserInfo(ApplyResponseDto applyResponseDto, User user) {
        TeamUserInfo teamUserInfo = new TeamUserInfo();
        teamUserInfo.setTeam(applyResponseDto.getTeam());
        teamUserInfo.setUser(user);
        teamUserInfo.setMembership(applyResponseDto.getMembership());
        teamUserInfo.setIsAccepted(applyResponseDto.getIsAccepted());
        teamUserInfo.setMessage(applyResponseDto.getMessage());
        teamUserInfo.setPortfolio(applyResponseDto.getPortfolio());
        return teamUserInfo;
    }


}

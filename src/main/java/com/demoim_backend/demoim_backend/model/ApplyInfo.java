package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class ApplyInfo {

    /*
    가지고 있어야할 element목록
    id(PK), userId(FK), teamId(FK), leader, member, isAccepted
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "applyInfo")
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

    public static ApplyInfo createTeamUserInfo(ApplyResponseDto applyResponseDto, User user) {
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setTeam(applyResponseDto.getTeam());
        applyInfo.setUser(user);
        applyInfo.setMembership(applyResponseDto.getMembership());
        applyInfo.setIsAccepted(applyResponseDto.getIsAccepted());
        applyInfo.setMessage(applyResponseDto.getMessage());
        applyInfo.setPortfolio(applyResponseDto.getPortfolio());
        return applyInfo;
    }


}

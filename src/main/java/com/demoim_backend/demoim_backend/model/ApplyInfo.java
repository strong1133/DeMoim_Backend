package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.ApplyResponseSaveDto;
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

    @Enumerated(EnumType.STRING)
    private ApplyState applyState;

    @Column
    private String message;

    @Column
    private String portfolio;


    public enum Membership {
        LEADER, MEMBER //Member라는 element 피하는게 좋을수도.. -> 대문자 처리
    }

    public enum ApplyState {
        WAITING, ACCEPTED, DENIED, LEADER
    }

    public static ApplyInfo createTeamUserInfo(ApplyResponseSaveDto applyResponseSaveDto, User user) {
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setTeam(applyResponseSaveDto.getTeam());
        applyInfo.setUser(user);
        applyInfo.setMembership(applyResponseSaveDto.getMembership());
        applyInfo.setApplyState(applyResponseSaveDto.getApplyState());
        applyInfo.setMessage(applyResponseSaveDto.getMessage());
        applyInfo.setPortfolio(applyResponseSaveDto.getPortfolio());
        return applyInfo;
    }


    public void choiceMember(ApplyState choice){
        this.applyState = choice;
    }
}

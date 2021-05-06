package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.TeamUserInfo;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyResponseDto {
    private Long id; //???
//    private Long teamId;
    private Team team;
    private String message;
    private String portfolio;
    private UserUpdateProfileSaveRequestDto applicant; //position은 이 user 안에 있음
    private TeamUserInfo.Membership membership;
    private Boolean isAccepted; // 초기화는 null, 거절은 false, 선택은 true 이렇게 가능하다면 bestcase.

//    public ApplyResponseDto(TeamUserInfo teamUserInfo, ApplyRequestDto applyRequestDto) {
    public ApplyResponseDto(User user, Team team, ApplyRequestDto applyRequestDto) {
//        this.id = teamUserInfo.getId();
//        this.teamId = teamUserInfo.getTeam().getId();
        this.team = team;
        this.message = applyRequestDto.getMessage();
        this.portfolio = applyRequestDto.getPortfolio();
//        this.user = new UserUpdateProfileSaveRequestDto(teamUserInfo.getUser());
        this.applicant = new UserUpdateProfileSaveRequestDto(user);
        this.membership = TeamUserInfo.Membership.MEMBER;
        this.isAccepted = false;
        //this.isAccepted
    }
    public ApplyResponseDto(Team team) {
        this.team = team;
        this.message = "내가 리더><";
        this.portfolio = "포폴없음!";
//        this.user = new UserUpdateProfileSaveRequestDto(teamUserInfo.getUser());
        this.applicant = new UserUpdateProfileSaveRequestDto(team.getLeader());
        this.membership = TeamUserInfo.Membership.LEADER;
        this.isAccepted = true;
    }

    public ApplyResponseDto(TeamUserInfo teamUserInfo) {
        this.id = teamUserInfo.getId();
        this.team = teamUserInfo.getTeam();
        this.message = teamUserInfo.getMessage();
        this.portfolio = teamUserInfo.getPortfolio();
//        this.user = new UserUpdateProfileSaveRequestDto(teamUserInfo.getUser());
        this.applicant = new UserUpdateProfileSaveRequestDto(teamUserInfo.getUser());
        this.membership = teamUserInfo.getMembership();
        this.isAccepted = teamUserInfo.getIsAccepted();
    }


}

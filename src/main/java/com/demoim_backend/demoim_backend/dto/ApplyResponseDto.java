package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyResponseDto {
    private Long id;
    private ApplyInfo.ApplyState applyState;
    private ApplyInfo.Membership membership;
    private String message;
    private String portfolio;
//    private TeamResponseDto teamResponseDto;
    private ResponseUser responseUser;

    @Builder
    public ApplyResponseDto(ApplyInfo applyInfo, ResponseUser responseUser) {
        this.id = applyInfo.getId();
        this.applyState = applyInfo.getApplyState();
        this.membership = applyInfo.getMembership();
        this.message = applyInfo.getMessage();
        this.portfolio = applyInfo.getPortfolio();
//        this.teamResponseDto = teamResponseDto;
        this.responseUser = responseUser;
    }
}

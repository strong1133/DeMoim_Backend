package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.ApplyInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyResponseDto {
    private Long applyId;
    private ApplyInfo.ApplyState applyState;
    private ApplyInfo.Membership membership;
    private String message;
    private String portfolio;
//    private TeamResponseDto teamResponseDto;
    private ResponseUserDto responseUser;

    @Builder
    public ApplyResponseDto(ApplyInfo applyInfo, ResponseUserDto responseUserDto) {
        this.applyId = applyInfo.getId();
        this.applyState = applyInfo.getApplyState();
        this.membership = applyInfo.getMembership();
        this.message = applyInfo.getMessage();
        this.portfolio = applyInfo.getPortfolio();
//        this.teamResponseDto = teamResponseDto;
        this.responseUser = responseUserDto;
    }
}

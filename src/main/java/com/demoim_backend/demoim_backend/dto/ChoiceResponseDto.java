package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class ChoiceResponseDto {
    private String msg;
    private String username;
    private String nickname;
    private String position;
    private List<Map<String,Integer>> remainSeat;

    private List<ApplyResponseDto> applicantList;

    @Builder
    public ChoiceResponseDto(String msg, User user, List<Map<String,Integer>> info, List<ApplyResponseDto> applyResponseDtoList ){
        this.msg = msg;
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.position = user.getPosition();
        this.remainSeat = info;
        this.applicantList = applyResponseDtoList;
    }
}

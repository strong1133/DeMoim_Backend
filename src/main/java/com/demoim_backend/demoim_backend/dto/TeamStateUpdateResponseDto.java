package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TeamStateUpdateResponseDto {

    private Team.StateNow recruitState;
    private Team.StateNow projectState;

    public TeamStateUpdateResponseDto(Team.StateNow recruitState, Team.StateNow projectState) {
        this.recruitState = recruitState;
        this.projectState = projectState;
    }

}

package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamStateUpdateResponseDto {

    private Team.StateNow recruitState;
    private Team.StateNow projectState;

}

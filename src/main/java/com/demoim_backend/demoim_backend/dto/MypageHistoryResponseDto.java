package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class MypageHistoryResponseDto {
   ActiveTeamResponseDto activeTeamResponseDto;
   List<FinishedTeamResponseDto> finishedTeamResponseDtoList;

   @Builder
   public MypageHistoryResponseDto(ActiveTeamResponseDto activeTeamResponseDto, List<FinishedTeamResponseDto> finishedTeamResponseDtoList ){
      this.activeTeamResponseDto = activeTeamResponseDto;
      this.finishedTeamResponseDtoList = finishedTeamResponseDtoList;
   }

}

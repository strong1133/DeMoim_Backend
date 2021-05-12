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
   List<ActiveTeamResponseDto>  activeTeamResponseDtoList;
   List<FinishedTeamResponseDto> finishedTeamResponseDtoList;

   @Builder
   public MypageHistoryResponseDto(List<ActiveTeamResponseDto>  activeTeamResponseDtoList, List<FinishedTeamResponseDto> finishedTeamResponseDtoList ){
      this.activeTeamResponseDtoList = activeTeamResponseDtoList;
      this.finishedTeamResponseDtoList = finishedTeamResponseDtoList;
   }

}

package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.ApplyRequestDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.TeamUserInfo;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.TeamUserInfoRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplyService {
    private final UserService userService;
//    private final Authentication authentication;
    private final TeamService teamService;
    private final TeamUserInfoRepository teamUserInfoRepository;

    //팀메이킹 모집글 지원
    public ApplyResponseDto applyTeam(Authentication authentication, Long teamId, ApplyRequestDto applyRequestDto) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
//        TeamUserInfo teamUserInfo = teamUserInfoRepository.findByTeamId(teamId);
        Team team = teamService.findTeam(teamId);

        ApplyResponseDto applyResponseDto = new ApplyResponseDto(user, team, applyRequestDto);
        TeamUserInfo teamUserInfo = TeamUserInfo.createTeamUserInfo(applyResponseDto, user);
        teamUserInfoRepository.save(teamUserInfo);
        return applyResponseDto; // teamuserInfo의 id 누락
    }

    //지원자 조회(리더에게만 보이는 권한 부여)
    public List<ApplyResponseDto> getApplications(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        TeamUserInfo leaderInfo =  teamUserInfoRepository.findByTeamIdAndMembership(teamId, TeamUserInfo.Membership.LEADER);

        if (user.equals(leaderInfo.getUser())) {
            List<TeamUserInfo> teamUserInfoList = teamUserInfoRepository.findAllByTeamId(teamId);




            List<ApplyResponseDto> applyResponseDtoList = new ArrayList<ApplyResponseDto>();
            for (TeamUserInfo teamUserInfo : teamUserInfoList) {
                ApplyResponseDto applyResponseDto = new ApplyResponseDto(teamUserInfo);
                applyResponseDtoList.add(applyResponseDto);
            }

            return applyResponseDtoList;
        } else {
                return null;
        }

    }

    //팀메이킹 모집글 지원취소
    public void cancelApplication(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
//        TeamUserInfo teamUserInfo = teamUserInfoRepository.findByTeamId(teamId);
        Team team = teamService.findTeam(teamId);

        TeamUserInfo teamUserInfo = teamUserInfoRepository.findByTeamIdAndUserId(teamId, user.getId());
        teamUserInfoRepository.delete(teamUserInfo);
    }
}

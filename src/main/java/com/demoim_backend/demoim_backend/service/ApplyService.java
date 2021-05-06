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
        TeamUserInfo leaderInfo =  teamUserInfoRepository.findByTeamIdAndMembership(teamId, TeamUserInfo.Membership.LEADER);
        Team team = teamService.findTeam(teamId);

        //회원정보가 해당모집글의 리더와 같다면 -> 에러
        if (user.equals(leaderInfo.getUser())) {
            return null;
        } else {
            ApplyResponseDto applyResponseDto = new ApplyResponseDto(user, team, applyRequestDto);
            TeamUserInfo teamUserInfo = TeamUserInfo.createTeamUserInfo(applyResponseDto, user);
            teamUserInfoRepository.save(teamUserInfo);
            return applyResponseDto; // teamuserInfo의 id 누락
        }

    }

    //지원자 조회(리더에게만 보이는 권한 부여)
    public List<ApplyResponseDto> getApplications(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        TeamUserInfo leaderInfo =  teamUserInfoRepository.findByTeamIdAndMembership(teamId, TeamUserInfo.Membership.LEADER);

        System.out.println("leaderInfo의 userId"+leaderInfo.getUser().getId());
        System.out.println("user의 userId"+user.getId());

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
    public String cancelApplication(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        List<TeamUserInfo> memberInfoList =  teamUserInfoRepository.findAllByTeamIdAndMembership(teamId, TeamUserInfo.Membership.MEMBER);
//        Team team = teamService.findTeam(teamId);

        //해당 모집글에 대해 신청한사람들의 목록에 해당 유저의 정보가 있는지를 할기 위해 신청멤버 정보목록 생성
        List<User> members = new ArrayList<User>();
        for (TeamUserInfo memberInfo : memberInfoList) {
            members.add(memberInfo.getUser());
        }

        if (members.contains(user)) {
            TeamUserInfo teamUserInfo = teamUserInfoRepository.findByTeamIdAndUserId(teamId, user.getId());
            teamUserInfoRepository.delete(teamUserInfo);
//            String success = "해당 게시물에 대한 지원취소가 완료되었습니다."
            return "success";
        } else {
//            "모집글에 대한 취소는 신청자만 할 수 있습니다.";
            return "fail";
        }

//

    }
}

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
    private final TeamService teamService;
    private final TeamUserInfoRepository teamUserInfoRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    //팀메이킹 모집글 지원 _ 리더인가? 이미 신청한 사람인가? 참여중인프로젝트가 2개 이하인가? 자기 파트에 모집이 덜됐을 때
    public ApplyResponseDto applyTeam(Authentication authentication, Long teamId, ApplyRequestDto applyRequestDto) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        TeamUserInfo leaderInfo =  teamUserInfoRepository.findByTeamIdAndMembership(teamId, TeamUserInfo.Membership.LEADER);
        Team team = teamService.findTeam(teamId);
        List<TeamUserInfo> teamUserInfoList = teamUserInfoRepository.findAllByTeamId(teamId);

        //해당 모집글의 구성원 목록 생성
        List<User> teamUsers = new ArrayList<User>();
        for (TeamUserInfo teamUserInfo : teamUserInfoList) {
            teamUsers.add(teamUserInfo.getUser());
        }

        //회원정보가 해당모집글의 참여자 목록에 있는지(리더 or 회원목록) 확인
        if (teamUsers.contains(user)) {
            if (user.equals(team.getLeader())) {
                throw new IllegalArgumentException("게시글 작성자는 본인의 글에 지원할 수 없습니다.");
            } else {
                throw new IllegalArgumentException("이미 지원한 게시글입니다.");
            }
        }

        //2개가 넘는지 여부 검사
        List<Long> teamIdListOfUser = teamUserInfoRepository.findTeamIdByUserId(user.getId());
        for (Long teamIdOfUser : teamIdListOfUser) {
            if (!teamRepository.existsByIdAndProjectState(teamIdOfUser, Team.StateNow.ACTIVATED)) {
                teamIdListOfUser.remove(teamIdOfUser);
            };
        }
        if (teamIdListOfUser.size() > 2L) {
            throw new IllegalArgumentException("겹치는 프로젝트 기간 내에 참여할 수 있는 프로젝트는 최대 2개 입니다.");
        }

        //지원하려는 team의 신청자 포지션이 다 찼을때 지원 막기
        String userPosition = user.getPosition();
        System.out.println("userPosition : "+userPosition);
        List<Long> acceptedTeamMemberIdList = teamUserInfoRepository.findUserIdByTeamIdAndMembershipAndIsAccepted(teamId, TeamUserInfo.Membership.MEMBER, true);
//        int numPositionApplied = 0;
        List<Long> samePositionMemberList = new ArrayList<Long>();
        for (Long acceptedTeamMemberId : acceptedTeamMemberIdList) {
            if (userPosition.equals(userRepository.findPositionById(acceptedTeamMemberId))) {
                samePositionMemberList.add(acceptedTeamMemberId);
            }
        }
        //팀메이킹 모집글의 해당 포지션 공고인원보다 확정된사람이 같거나 더 많은 경우
        int numPosition;
        System.out.println("userPosition = 프론트엔드? : "+userPosition.equals("프론트엔드"));
        System.out.println("프론트엔드");
        System.out.println("userPosition = 백엔드? : "+userPosition.equals("백엔드"));
        System.out.println("userPosition = 디자이너? : "+userPosition.equals("디자이너"));
        System.out.println("userPosition = 기획자? : "+userPosition.equals("기획자"));
        switch(userPosition) {
            case "프론트엔드":
                numPosition = team.getFront();
                break;
            case "백엔드":
                numPosition = team.getBack();
                break;
            case "디자이너":
                numPosition = team.getDesigner();
                break;
            case "기획자":
                numPosition = team.getPlanner();
                break;
            default:
                throw new IllegalArgumentException("프론트엔드 / 백엔드 / 디자이너 / 기획자 중에 올바르게 프로필을 수정해주세요.");
        }

        if (samePositionMemberList.size() >= numPosition) {
            throw new IllegalArgumentException("해당 글에 대한 회원님 포지션 모집이 마갑되었습니다.");
        }
//        Boolean alreadyFull = teamRepository.findByTeamIdAnd


        ApplyResponseDto applyResponseDto = new ApplyResponseDto(user, team, applyRequestDto);
        TeamUserInfo teamUserInfo = TeamUserInfo.createTeamUserInfo(applyResponseDto, user);
        teamUserInfoRepository.save(teamUserInfo);
        return applyResponseDto; // teamuserInfo의 id 누락

//            List<TeamUserInfo> teamsInfoOfUser= teamUserInfoRepository.findAllByUserId(user.getId());
//            Long numTeamsofUser = teamRepository.count(teamRepository.findByUser)



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
            throw new IllegalArgumentException("게시글의 작성자만 지원한 사람 목록을 볼 수 있습니다.");
        }

    }

    //팀메이킹 모집글 지원취소
    public String cancelApplication(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        //Membership이 LEADER가 아닌, MEMBER에 대한 목록만 조회
        List<TeamUserInfo> memberInfoList = teamUserInfoRepository.findAllByTeamIdAndMembership(teamId, TeamUserInfo.Membership.MEMBER);
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

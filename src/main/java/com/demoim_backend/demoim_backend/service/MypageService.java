package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.*;
import com.demoim_backend.demoim_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final UserService userService;
    private final ApplyInfoRepository applyInfoRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final SmallTalkRepository smallTalkRepository;
    private final SmallTalkService smallTalkService;

    // 로그인된 유저의 스몰토크 조회
    public List<SmallTalkResponseDto> findMySmallTalk(Authentication authentication) {

        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        Long userId = user.getId();

        List<SmallTalk> smallTalk = smallTalkRepository.findAllBySmallTalkUserId(userId);

        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk1 : smallTalk) {
            smallTalkListResponseDtoList.add(smallTalkService.entityToDto(smallTalk1));
        }

        return smallTalkListResponseDtoList;

    }


    // 로그인된 유저의 자랑하기 조회
    public List<ExhibitionResponseDto> findMyExhibition(Authentication authentication) {

        User user = userService.findMyUserInfo(authentication);
        Long userId = user.getId();

        List<Exhibition> exhibitions = exhibitionRepository.findAllByExhibitionUserId(userId);

        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        ExhibitionResponseDto exhibitionResponseDto = new ExhibitionResponseDto();
        for (Exhibition exhibition : exhibitions) {
            exhibitionResponseDtoList.add(exhibitionResponseDto.entityToDto(exhibition));
        }

        return exhibitionResponseDtoList;

    }

    // 마이페이지의 히스토리 _ 현재유저가 지원한 프로젝트 보기
    public List<TeamResponseDto> findMyApply(Authentication authentication) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        List<ApplyInfo> applyInfoList = applyInfoRepository.findTeamIdByUserIdAndMembershipAndApplyState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.WAITING);
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();
        for (ApplyInfo applyInfo : applyInfoList) {
            Long teamId = applyInfo.getTeam().getId();
            Team team = teamRepository.findById(teamId).orElseThrow(
                    () -> new IllegalArgumentException("해당 팀이 없습니다.")

            );
            UserUpdateProfileSaveRequestDto userUpdateProfileSaveRequestDto = new UserUpdateProfileSaveRequestDto(team.getLeader());
            TeamResponseDto teamResponseDto = new TeamResponseDto(team, userUpdateProfileSaveRequestDto);
            teamResponseDtoList.add(teamResponseDto);
        }
        return teamResponseDtoList;
    }

    //마이페이지의 히스토리 _ 참여 프로젝트 (진행중 1개 + 참여했던 프로젝트 다수) 조회
    public Map<String, Object> findMyActivedTeam(Authentication authentication) {
//    public List<ActiveTeamResponseDto> findMyActivedTeam(Authentication authentication) {
        // - ActiveTeamResponseDto 타입의 현재 진행중 프로젝트 하나(projectState = ACTIVATED)
        // - List<ActiveTeamResponseDto> 타입의 참여했던 프로젝트 여럿(projectState = FINISHED)

        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        ActiveTeamResponseDto activatedTeamResponseDto = new ActiveTeamResponseDto();
        List<ActiveTeamResponseDto> finishedTeamResponseDtoList = new ArrayList<>();
//        List<ActiveTeamResponseDto> activeTeamResponseDtoList = new ArrayList<>();
//        List<ApplyInfo> applyInfoList= applyInfoRepository.findTeamIdByUserIdAndApplyStateOrMembership(user.getId() , ApplyInfo.ApplyState.ACCEPTED, ApplyInfo.Membership.LEADER);
        //본인이 리더건 멤버건 진행중과 참여했던 프로젝트에서 구분할 필요는 없으르면 Accepted 기준 전체 조회
        List<ApplyInfo> myApplyInfoList = applyInfoRepository.findAllByUserIdAndApplyState(user.getId(), ApplyInfo.ApplyState.ACCEPTED);
        System.out.println("applyInfoList :" + myApplyInfoList);
        List<ResponseUserDto> memberList = new ArrayList<>();
//        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();

        for (ApplyInfo myApplyInfo : myApplyInfoList) {
            Team team = teamRepository.findById(myApplyInfo.getTeam().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 팀이 없습니다.")
            );
            User userInfo = userRepository.findById(myApplyInfo.getUser().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 없습니다.")
            );
            //if 현재 진행중인 프로젝트의 경우, else 끝난 프로젝트들
            List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);
            for (ApplyInfo memberApplyInfo : membersApplyInfoList) {
                User member = memberApplyInfo.getUser();
                ResponseUserDto responseUserDto = new ResponseUserDto().entityToDto(member);
                memberList.add(responseUserDto);
            }
            System.out.println("memberList :" + memberList);
            if (team.getProjectState() == Team.StateNow.ACTIVATED) {
                activatedTeamResponseDto = new ActiveTeamResponseDto(team, memberList);
            }
            finishedTeamResponseDtoList.add(new ActiveTeamResponseDto(team, memberList));
//            activeTeamResponseDtoList = activeTeamResponseDtoList.????

            // 여기부터 작업.....
//            ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto(team, memberList);
//            List<ActiveTeamResponseDto> finishedTeamResponseDto = new ArrayList<>();
//            finishedTeamResponseDto.add(activeTeamResponseDto);
//            activeTeamResponseDtoList.add(activeTeamResponseDto);
        }
        Map<String, Object> joinedProjects = new HashMap<>();
        joinedProjects.put("activatedProject",activatedTeamResponseDto);
        joinedProjects.put("finishedProject", finishedTeamResponseDtoList);

        return joinedProjects;
        //멤버를 넣어서 보내주지만 그 멤버에 대한 프로필이미지, nickname, position을 프론트엔드에서 가져올수 있는지 확인할것
    }

    //마이페이지 히스토리 _ 내가 리더인 프로젝트 조회하기
    public ActiveTeamResponseDto findMyTeamAsLeader(Authentication authentication) {
        //유저정보 검증
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        ApplyInfo myTeamAsLeader = applyInfoRepository.findByUserIdAndMembership(user.getId(), ApplyInfo.Membership.LEADER);


//        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();
        Team team = teamRepository.findById(myTeamAsLeader.getTeam().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 팀이 없습니다.")
        );
        User leader = userRepository.findById(myTeamAsLeader.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        List<ResponseUserDto> memberList = new ArrayList<>();
        //여기서도 리더를 포함시켜서 노출?(우선 위와 동일하게 리더도 포함됨)
        List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);
        for (ApplyInfo memberApplyInfo : membersApplyInfoList) {
            User member = memberApplyInfo.getUser();
            ResponseUserDto responseUserDto = new ResponseUserDto().entityToDto(member);
            memberList.add(responseUserDto);
        }
        System.out.println("memberList :" + memberList);
//            activeTeamResponseDtoList = activeTeamResponseDtoList.????
        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto(team, memberList);

        return activeTeamResponseDto;
    }
}

//    //현재 참여중인 프로젝트 보기
//    public List<ActiveTeamResponseDto> findMyActiveTeam(Authentication authentication){
//        User user = userService.findCurUser(authentication).orElseThrow(
//                ()-> new IllegalArgumentException("해당 유저가 없습니다.")
//        );
//        List<ActiveTeamResponseDto> activeTeamResponseDtoList = new ArrayList<>();
//        List<ApplyInfo> applyInfoList= applyInfoRepository.findTeamIdByUserIdAndApplyStateOrMembership(user.getId() , ApplyInfo.ApplyState.ACCEPTED, ApplyInfo.Membership.LEADER);
//        System.out.println("applyInfoList :" +applyInfoList);
//        List<User> memberList   = new ArrayList<>();
//        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();
//        for (ApplyInfo applyInfo: applyInfoList){
//            Team team = teamRepository.findById(applyInfo.getTeam().getId()).orElseThrow(
//                    ()-> new IllegalArgumentException("해당 팀이 없습니다.")
//            );
//            User member = userRepository.findById(applyInfo.getUser().getId()).orElseThrow(
//                    ()-> new IllegalArgumentException("해당 유저가 없습니다.")
//            );
//            memberList.add(member);
//            System.out.println("maemberList :" + memberList);
//            activeTeamResponseDtoList = activeTeamResponseDtoList.
//            activeTeamResponseDtoList.add(activeTeamResponseDto(team, memberList));
//        }
//        return activeTeamResponseDtoList;
//    }
//}

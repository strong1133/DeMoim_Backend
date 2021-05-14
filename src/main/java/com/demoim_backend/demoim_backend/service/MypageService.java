package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.Exception.GlobalExceptionHandler;
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

    private ActiveTeamResponseDto setActiveDto(Team team, List<ResponseUserDto> memberList) {
        return new ActiveTeamResponseDto(team, memberList);
    }

    // 로그인된 유저의 스몰토크 조회
    public List<SmallTalkResponseDto> findMySmallTalk(Long id) {

        User user = userService.findTargetUser(id);
        Long userId = user.getId();

        List<SmallTalk> smallTalk = smallTalkRepository.findAllBySmallTalkUserId(userId);

        List<SmallTalkResponseDto> smallTalkListResponseDtoList = new ArrayList<>();

        for (SmallTalk smallTalk1 : smallTalk) {
            smallTalkListResponseDtoList.add(smallTalkService.entityToDto(smallTalk1));
        }

        return smallTalkListResponseDtoList;

    }


    // 로그인된 유저의 자랑하기 조회
    public List<ExhibitionResponseDto> findMyExhibition(Long id) {

        User user = userService.findTargetUser(id);
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
    public List<TeamResponseDto> findMyApply(Long id) {
        User user = userService.findTargetUser(id);
        List<ApplyInfo> applyInfoList = applyInfoRepository.findTeamIdByUserIdAndMembership(user.getId(), ApplyInfo.Membership.MEMBER);
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
    public Map<String, Object> findMyActivedTeam(Long id) {

        User user = userService.findTargetUser(id);
        ActiveTeamResponseDto activatedTeamResponseDto = new ActiveTeamResponseDto();
        List<ActiveTeamResponseDto> finishedTeamResponseDtoList = new ArrayList<>();
        List<ActiveTeamResponseDto> activeTeamResponseDtoList = new ArrayList<>();
        List<ApplyInfo> myApplyInfoList = applyInfoRepository.findAllByUserIdAndApplyStateOrUserIdAndMembership(user.getId(), ApplyInfo.ApplyState.ACCEPTED,
                user.getId(), ApplyInfo.Membership.LEADER);

        System.out.println("applyInfoList :" + myApplyInfoList);
        //멤버리스트 생성
        //memberList for문 안에 넣기 18:12
//        List<ResponseUserDto> memberList = new ArrayList<>();


        for (ApplyInfo myApplyInfo : myApplyInfoList) {
            Team team = teamRepository.findById(myApplyInfo.getTeam().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 팀이 없습니다.")
            );
            User userInfo = userRepository.findById(myApplyInfo.getUser().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 없습니다.")
            );
            //for문 안에 memberList 생성(18:19)
            List<ResponseUserDto> memberList = new ArrayList<>();

            //memberList에 리더의 정보 먼저 입력
            ResponseUserDto leaderDto = ResponseUserDto.builder().build().entityToDto(team.getLeader());
            memberList.add(leaderDto);


            //if 현재 진행중인 프로젝트의 경우, else 끝난 프로젝트들
            List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);


            for (ApplyInfo memberApplyInfo : membersApplyInfoList) {
                User member = memberApplyInfo.getUser();
                User leader = userRepository.findById(memberApplyInfo.getTeam().getLeader().getId()).orElseThrow(
                        () -> new IllegalArgumentException("해당 유저가 없습니다.")
                );
                //위에서 멤버로서 조회했기때문에 리더와 같을 가능성은 없을듯

                ResponseUserDto memberDto = ResponseUserDto.builder().build().entityToDto(member);
                memberList.add(memberDto);

//                ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(leader);
//                memberList.add(responseUserDto);

            }
            System.out.println("memberList :" + memberList);

            if (team.getProjectState() == Team.StateNow.ACTIVATED || team.getProjectState() == Team.StateNow.YET) {
                activatedTeamResponseDto = new ActiveTeamResponseDto(team, memberList);
                activeTeamResponseDtoList.add(activatedTeamResponseDto);
            } else {
                finishedTeamResponseDtoList.add(new ActiveTeamResponseDto(team, memberList));
            }
        }

        Map<String, Object> joinedProjects = new HashMap<>();
        if (activatedTeamResponseDto.getTeamId() == null) {
            joinedProjects.put("activatedProject", null);
        } else {
            joinedProjects.put("activatedProject", activeTeamResponseDtoList);
        }
        joinedProjects.put("finishedProject", finishedTeamResponseDtoList);
        return joinedProjects;

    }

    //마이페이지 히스토리 _ 내가 리더인 프로젝트 조회하기
    public List<ActiveTeamResponseDto> findMyTeamAsLeader(Long id) throws NoSuchFieldException {
        //유저정보 검증
        User user = userService.findTargetUser(id);
        List<ApplyInfo> myTeamAsLeaderList = applyInfoRepository.findByUserIdAndMembershipAndTeamProjectStateNot(user.getId(), ApplyInfo.Membership.LEADER, Team.StateNow.FINISHED);
        List<ActiveTeamResponseDto> activeTeamResponseDtoList = new ArrayList<>();

        if (myTeamAsLeaderList == null) {
            throw new NoSuchFieldException("Null");
        }
        for (ApplyInfo myTeamAsLeader : myTeamAsLeaderList) {
            Team team = teamRepository.findById(myTeamAsLeader.getTeam().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 팀이 없습니다.")
            );
            User leader = userRepository.findById(myTeamAsLeader.getUser().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 없습니다.")
            );
            List<ResponseUserDto> memberList = new ArrayList<>();
            List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);

            ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();
            for (ApplyInfo memberApplyInfo : membersApplyInfoList) {

                User member = memberApplyInfo.getUser();
                ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(member);
                memberList.add(responseUserDto);
            }
            ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(leader);
            memberList.add(responseUserDto);
            activeTeamResponseDto = setActiveDto(team, memberList);
            activeTeamResponseDtoList.add(activeTeamResponseDto);
        }


        return activeTeamResponseDtoList;
    }


}

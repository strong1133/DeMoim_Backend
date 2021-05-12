package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.Exception.GlobalExceptionHandler;
import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.*;
import com.demoim_backend.demoim_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public MypageHistoryResponseDto findMyHistoryTeam(Authentication authentication) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        System.out.println("11");
//        ApplyInfo myTeamAsLeader = applyInfoRepository.findByUserIdAndMembership(user.getId(), ApplyInfo.Membership.LEADER);
        System.out.println(user.getId());
        List<ApplyInfo> applyInfoList = applyInfoRepository.findTeamIdByUserIdAndMembershipAndApplyState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED);

        System.out.println("22");
        List<ResponseUserDto> memberList = new ArrayList<>();
        System.out.println("33");
        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();
        for (ApplyInfo applyInfo : applyInfoList) {
            System.out.println("applyInfoList:" + applyInfo.getTeam().getId());
            Team team = teamRepository.findByIdAndProjectState(applyInfo.getTeam().getId(), Team.StateNow.ACTIVATED);
            User leader = userRepository.findById(applyInfo.getTeam().getLeader().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 유저가 없습니다.")
            );
            List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);
            for (ApplyInfo memberApplyInfo : membersApplyInfoList) {
                User member = memberApplyInfo.getUser();
                ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(member);
                memberList.add(responseUserDto);
            }
            ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(leader);
            memberList.add(responseUserDto);
            activeTeamResponseDto = new ActiveTeamResponseDto(team, memberList);
        }
        List<MypageHistoryResponseDto> mypageHistoryResponseDtoList = new ArrayList<>();
        List<FinishedTeamResponseDto> finishedTeamResponseDtoList = new ArrayList<>();

        return new MypageHistoryResponseDto(activeTeamResponseDto, finishedTeamResponseDtoList);

    }

    //마이페이지 히스토리 _ 내가 리더인 프로젝트 조회하기
    public ActiveTeamResponseDto findMyTeamAsLeader(Authentication authentication) throws NoSuchFieldException {
        //유저정보 검증
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        ApplyInfo myTeamAsLeader = applyInfoRepository.findByUserIdAndMembership(user.getId(), ApplyInfo.Membership.LEADER);
        System.out.println("3");
        if (myTeamAsLeader == null) {
            throw new NoSuchFieldException("asd");
        }


//        ActiveTeamResponseDto activeTeamResponseDto = new ActiveTeamResponseDto();
        Team team = teamRepository.findById(myTeamAsLeader.getTeam().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 팀이 없습니다.")
        );
        System.out.println("4");
        User leader = userRepository.findById(myTeamAsLeader.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );
        List<ResponseUserDto> memberList = new ArrayList<>();
        System.out.println("1");
        List<ApplyInfo> membersApplyInfoList = applyInfoRepository.findAllByteamIdAndApplyState(team.getId(), ApplyInfo.ApplyState.ACCEPTED);
        System.out.println("2");
        for (ApplyInfo memberApplyInfo : membersApplyInfoList) {
            User member = memberApplyInfo.getUser();
            ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(member);
            memberList.add(responseUserDto);
        }
        ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(leader);
        memberList.add(responseUserDto);


        System.out.println("memberList :" + memberList);
//            activeTeamResponseDtoList = activeTeamResponseDtoList.????

        return new ActiveTeamResponseDto(team, memberList);
    }
}

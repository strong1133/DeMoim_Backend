package com.demoim_backend.demoim_backend.service;

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


    // 로그인된 유저의 자랑기 조회
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

    // 마이페이지_현재유저가 지원한 프로젝트 보기
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
}

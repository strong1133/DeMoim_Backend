package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ApplyService {
    private final UserService userService;
    private final TeamService teamService;
    private final ApplyInfoRepository applyInfoRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    //알람 메이커
    public void alarmMaker(String commentsAlarm, User user, Team team) {
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(team.getLeader().getId());
        alarmRequestDto.setContents(commentsAlarm);
        if (!user.getId().equals(team.getLeader().getId())) {
            alarmService.createAlarm(alarmRequestDto);
        }
    }

    public ApplyResponseDto entityToDto(ApplyInfo applyInfo, ResponseUser responseUser) {
        ApplyResponseDto applyResponseDto = new ApplyResponseDto(applyInfo, responseUser);
        return applyResponseDto;
    }

    //팀메이킹 모집글 지원 _ 리더인가? 이미 신청한 사람인가? 참여중인프로젝트가 2개 이하인가? 자기 파트에 모집이 덜됐을 때
    public Map<String, String> applyTeam(Authentication authentication, Long teamId, ApplyRequestDto applyRequestDto) {
        System.out.println("================================================");
        Map<String, String> map = new HashMap<>();
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        ApplyInfo leaderInfo =  applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);
        System.out.println("leaderInfo : "+  leaderInfo);
        Team team = teamService.findTeam(teamId);
        List<ApplyInfo> applyInfoList = applyInfoRepository.findAllByTeamId(teamId);
        System.out.println("applyInfoList : "+ applyInfoList);


        //해당 모집글의 구성원 목록 생성
        List<User> teamUsers = new ArrayList<User>();
        System.out.println("===============================");
        System.out.println("teamUsers : "+teamUsers);
        for (ApplyInfo applyInfo : applyInfoList) {
            teamUsers.add(applyInfo.getUser());
        }
        System.out.println("1");
        //회원정보가 해당모집글의 참여자 목록에 있는지(리더 or 회원목록) 확인
        if (teamUsers.contains(user)) {
            if (user.equals(team.getLeader())) {
                throw new IllegalArgumentException("게시글 작성자는 본인의 글에 지원할 수 없습니다.");
            } else {
                throw new IllegalArgumentException("이미 지원한 게시글입니다.");
            }
        }
        System.out.println("2");
        //2개가 넘는지 여부 검사
        List<ApplyInfo> teamIdListOfUser = applyInfoRepository.findTeamIdByUserId(user.getId());
        System.out.println("teamIdListOfUser : "+ teamIdListOfUser);
        System.out.println("3909");
//        for (ApplyInfo teamIdOfUser : teamIdListOfUser) {
//            System.out.println("teamIdOfUser : " + teamIdOfUser);
//            System.out.println("4");
//            if (!teamRepository.existsByIdAndProjectState(teamIdOfUser.getTeam().getId(), Team.StateNow.ACTIVATED)) {
//                teamIdListOfUser.remove(teamIdOfUser);
//                System.out.println("5");
//            };
//        }
        if (teamIdListOfUser.size() > 2) {
            throw new IllegalArgumentException("겹치는 프로젝트 기간 내에 참여할 수 있는 프로젝트는 최대 2개 입니다.");
        }
        System.out.println("6");
        //지원하려는 team의 신청자 포지션이 다 찼을때 지원 막기
        String userPosition = user.getPosition();
        System.out.println("userPosition : "+userPosition);
        List<Long> acceptedTeamMemberIdList = applyInfoRepository.findUserIdByTeamIdAndMembershipAndIsAccepted(teamId, ApplyInfo.Membership.MEMBER, true);
//        int numPositionApplied = 0;
        List<Long> samePositionMemberList = new ArrayList<Long>();
        for (Long acceptedTeamMemberId : acceptedTeamMemberIdList) {
            if (userPosition.equals(userRepository.findPositionById(acceptedTeamMemberId))) {
                samePositionMemberList.add(acceptedTeamMemberId);
            }
        }
        //팀메이킹 모집글의 해당 포지션 공고인원보다 확정된사람이 같거나 더 많은 경우
        int numPosition = 0;
        System.out.println("userPosition : " + userPosition);
        System.out.println("userPosition = 프론트엔드? : "+userPosition.contentEquals("프론트엔드"));
        System.out.println("userPosition = 백엔드? : "+userPosition.contentEquals("백앤드"));
        System.out.println("userPosition = 디자이너? : "+userPosition.contentEquals("디자이너"));
        System.out.println("userPosition = 기획자? : "+userPosition.contentEquals("기획자"));
        
        if (userPosition.contentEquals("프론트앤드")){
            numPosition = team.getFront();
        }else if(userPosition.contentEquals("백앤드")){
            numPosition = team.getBack();
        }else if(userPosition.contentEquals("디자이너")){
            numPosition = team.getDesigner();
        }else if(userPosition.contentEquals("기획자")){
            numPosition = team.getPlanner();
        }

        if (samePositionMemberList.size() >= numPosition) {
            throw new IllegalArgumentException("해당 글에 대한 회원님 포지션 모집이 마갑되었습니다.");
        }
//        Boolean alreadyFull = teamRepository.findByTeamIdAnd


        ApplyResponseSaveDto applyResponseSaveDto = new ApplyResponseSaveDto(user, team, applyRequestDto);
        ApplyInfo applyInfo = ApplyInfo.createTeamUserInfo(applyResponseSaveDto, user);
        System.out.println("applyInfo :" +applyInfo);
        applyInfoRepository.save(applyInfo);

        //알람 생성
        String commentsAlarm = user.getNickname() + "님 께서 " + team.getTitle() + " 공고에 지원하셨습니다.";
        alarmMaker(commentsAlarm, user, team);

        map.put("msg",team.getTitle()+"공고에 지원이 완료되었습니다.");
        return  map;

//            List<ApplyInfo> teamsInfoOfUser= teamUserInfoRepository.findAllByUserId(user.getId());
//            Long numTeamsofUser = teamRepository.count(teamRepository.findByUser)



    }

    //지원자 조회(리더에게만 보이는 권한 부여)
    public List<ApplyResponseDto> getApplications(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        ApplyInfo leaderInfo =  applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);

        System.out.println("leaderInfo의 userId"+leaderInfo.getUser().getId());
        System.out.println("user의 userId"+user.getId());

        if (user.equals(leaderInfo.getUser())) {
            List<ApplyInfo> applyInfoList = applyInfoRepository.findAllByTeamId(teamId);
//            UserUpdateProfileSaveRequestDto userUpdateProfileSaveRequestDto = new UserUpdateProfileSaveRequestDto(user);
//            Team team = teamService.findTeam(leaderInfo.getTeam().getId());
//            TeamResponseDto teamResponseDto = new TeamResponseDto(team,userUpdateProfileSaveRequestDto);
            ResponseUser responseUser = ResponseUser.builder().build().entityToDto(user);


            List<ApplyResponseDto> applyResponseDtoList = new ArrayList<>();
            for (ApplyInfo applyInfo : applyInfoList) {
                ApplyResponseDto applyResponseDto = entityToDto(applyInfo,responseUser);
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
        List<ApplyInfo> memberInfoList = applyInfoRepository.findAllByTeamIdAndMembership(teamId, ApplyInfo.Membership.MEMBER);
//        Team team = teamService.findTeam(teamId);

        //해당 모집글에 대해 신청한사람들의 목록에 해당 유저의 정보가 있는지를 할기 위해 신청멤버 정보목록 생성
        List<User> members = new ArrayList<User>();
        for (ApplyInfo memberInfo : memberInfoList) {
            members.add(memberInfo.getUser());
        }

        if (members.contains(user)) {
            ApplyInfo applyInfo = applyInfoRepository.findByTeamIdAndUserId(teamId, user.getId());
            applyInfoRepository.delete(applyInfo);
//            String success = "해당 게시물에 대한 지원취소가 완료되었습니다."
            return "success";
        } else {
//            "모집글에 대한 취소는 신청자만 할 수 있습니다.";
            return "fail";
        }

//

    }
}

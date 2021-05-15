package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final AlarmService alarmService;


    // ApplyResponseDto 메이커
    public ApplyResponseDto entityToDto(ApplyInfo applyInfo, ResponseUserDto responseUserDto) {
        return new ApplyResponseDto(applyInfo, responseUserDto);
    }

    // 모집 포지션 체커
    public int checkPosition(String applyPosition, Team team) {
        int numPosition = 0;

        if (applyPosition.contentEquals("프론트엔드")) {
            numPosition = team.getFront();

        } else if (applyPosition.contentEquals("백엔드")) {
            numPosition = team.getBack();

        } else if (applyPosition.contentEquals("디자이너")) {
            numPosition = team.getDesigner();

        } else if (applyPosition.contentEquals("기획자")) {
            numPosition = team.getPlanner();
        }
        return numPosition;
    }

    // 현재 지원 하는 포지션 인원 체크
    public int curPositionCnt(Team team, String position) {
        return applyInfoRepository.countByTeamIdAndApplyStateAndUserPositionAndMembership(team.getId(), ApplyInfo.ApplyState.ACCEPTED, position, ApplyInfo.Membership.MEMBER);
    }

    //팀메이킹 모집글 지원 _ 리더인가? 이미 신청한 사람인가? 참여중인프로젝트가 2개 이하인가? 자기 파트에 모집이 덜됐을 때
    public Map<String, String> applyTeam(Authentication authentication, Long teamId, ApplyRequestDto applyRequestDto) {

        System.out.println("================================================");

        Map<String, String> map = new HashMap<>();

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findMyUserInfo(authentication);

        Team team = teamService.findTeam(teamId);

        // 리더인지 파악
        ApplyInfo leaderInfo = applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);
        System.out.println("ApplyService의 leaderInfo : " + leaderInfo);

        List<ApplyInfo> applyInfoList = applyInfoRepository.findAllByTeamId(teamId);
        System.out.println("ApplyService의 applyInfoList : " + applyInfoList);


        //해당 모집글의 구성원 목록 생성
        List<User> teamUsers = new ArrayList<User>();
        for (ApplyInfo applyInfo : applyInfoList) {
            teamUsers.add(applyInfo.getUser());
        }

        //회원정보가 해당모집글의 참여자 목록에 있는지(리더 or 회원목록) 확인
        if (teamUsers.contains(user)) {
            if (user.equals(team.getLeader())) {
                throw new IllegalArgumentException("게시글 작성자는 본인의 글에 지원할 수 없습니다.");
            } else {
                throw new IllegalArgumentException("이미 지원한 게시글입니다.");
            }
        }

        //지원하려는 team의 신청자 포지션이 다 찼을때 지원 막기
        String userPosition = user.getPosition();

        int curPositionCnt = applyInfoRepository.countByTeamIdAndApplyStateAndUserPositionAndMembership(teamId,
                ApplyInfo.ApplyState.ACCEPTED, userPosition, ApplyInfo.Membership.MEMBER);

        //팀메이킹 모집글에 유저의 포지션이 꽉찼거나 0명인 경우
        int numPosition = checkPosition(userPosition, team);
        if (numPosition == 0) {
            throw new IllegalArgumentException("해당 글은 회원님의 포지션을 모집하지 않습니다.");
        } else if (curPositionCnt >= numPosition) {
            throw new IllegalArgumentException("해당 글에 대한 회원님 포지션 모집이 마갑되었습니다.");
        }

        ApplyResponseSaveDto applyResponseSaveDto = new ApplyResponseSaveDto(user, team, applyRequestDto);

        //여기서 state의 기본값은 WAITING으로 해주고, 이 값은 지원자의 경우 아래의 지원취소 API에서, 리더 본인의 경우 team
        ApplyInfo applyInfo = ApplyInfo.createTeamUserInfo(applyResponseSaveDto, user);
        applyInfoRepository.save(applyInfo);

        //알람 생성
        String commentsAlarm = user.getNickname() + "님 께서 " + team.getTitle() + " 공고에 지원하셨습니다.";
        alarmService.alarmMaker(commentsAlarm, user, team);

        map.put("msg", team.getTitle() + "공고에 지원이 완료되었습니다.");
        return map;
    }


    //지원자 조회(리더에게만 보이는 권한 부여)
    public List<ApplyResponseDto> getApplications(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findMyUserInfo(authentication);
        Team team = teamService.findTeam(teamId);

        ApplyInfo leaderInfo = applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);

        // 리더인 경우 에만 응답을 받을 수 있음.
        if (user.equals(leaderInfo.getUser())) {
            List<ApplyInfo> applyInfoList = applyInfoRepository.findAllByTeamIdAndMembershipAndApplyState(teamId, ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.WAITING);
            List<ApplyResponseDto> applyResponseDtoList = new ArrayList<>();
            for (ApplyInfo applyInfo : applyInfoList) {
                ResponseUserDto responseUserDto = ResponseUserDto.builder().build().entityToDto(userService.findTargetUser(applyInfo.getUser().getId()));
                ApplyResponseDto applyResponseDto = entityToDto(applyInfo, responseUserDto);
                applyResponseDtoList.add(applyResponseDto);
            }
            return applyResponseDtoList;
        } else {
            throw new IllegalArgumentException("게시글의 작성자만 지원한 사람 목록을 볼 수 있습니다.");
        }

    }

    //팀메이킹 모집글 지원취소
    public Map<String, String> cancelApplication(Authentication authentication, Long teamId) {
        Map<String, String> message = new HashMap<>();
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findMyUserInfo(authentication);
        //Team 정보 검증
        Team team = teamService.findTeam(teamId);

        //해당 지원을 취소할 수 있는 자격(멤버이면서 ACCEPTED인지) 확인 -> userId, teamId, membership = MEMBER, applyState=ACCEPTED인 상황에서 삭제 진행가능
        ApplyInfo userApplyInfo = applyInfoRepository.findByTeamIdAndUserId(teamId, user.getId());

        //지원부분(POST)은 equals 메소드로 동등성 비교를 함.
        if (userApplyInfo == null) {
            throw new IllegalArgumentException("팀 참여자 목록에서 회원님의 정보를 찾을 수 없습니다."); //얘 불필요한건지.. 어디서 걸러지나요?
        } else if (userApplyInfo.getMembership() != ApplyInfo.Membership.MEMBER) {
            throw new IllegalArgumentException("게시글 작성자입니다.");
        }

        applyInfoRepository.delete(userApplyInfo);

        //알람 생성
        String commentsAlarm = user.getNickname() + "님 께서 " + team.getTitle() + " 공고에 지원을 취소하셨습니다.";
        User leader = team.getLeader();
        alarmService.alarmMakerForLeader(commentsAlarm, leader);

        message.put("msg", "해당 게시물에 대한 지원취소가 완료되었습니다.");
        return message;
    }


    // 리더의 선택
    @Transactional
    public ChoiceResponseDto choiceMember(Authentication authentication, Long applyId) {
        ApplyInfo applyInfo = applyInfoRepository.findById(applyId).orElseThrow(
                () -> new IllegalArgumentException("해당 지원정보가 없습니다.")
        );

        User user = userService.findMyUserInfo(authentication);
        Team team = teamService.findTeam(applyInfo.getTeam().getId());

        Long teamLeader = team.getLeader().getId();
        Long curUser = user.getId();
        if (!curUser.equals(teamLeader)) {
            throw new IllegalArgumentException("당신은 리더가 아닙니다.");
        }

        String applyPosition = applyInfo.getUser().getPosition();
        int curPositionCnt = curPositionCnt(team, applyPosition);
        int numPosition = checkPosition(applyPosition, team);


        if (curPositionCnt >= numPosition) {
            throw new IllegalArgumentException("해당 포지션은 모집이 완료됬습니다.");
        }
        //흐름 : 먼저 ACCEPTED 처리해주고나서 해당 지원자의 다른 지원정보를 싹 업데이트해주는 순서(서로겹칠만한 프로젝트는 여기서 찾아 DENIED로 바꿔주기
        applyInfo.choiceMember(ApplyInfo.ApplyState.ACCEPTED);

        // 포지션 별 남은 자리 반환
        List<Map<String, Integer>> info = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int front = team.getFront() - curPositionCnt(team, "프론트엔드");
        int back = team.getBack() - curPositionCnt(team, "백엔드");
        System.out.println("back :" + team.getBack() + "-" + curPositionCnt(team, "백엔드"));
        int designer = team.getDesigner() - curPositionCnt(team, "디자이너");
        int planner = team.getPlanner() - curPositionCnt(team, "기획자");
        map.put("Front", front);
        map.put("Back", back);
        map.put("Designer", designer);
        map.put("Planner", planner);

        info.add(map);
        String msg = applyInfo.getUser().getNickname() + "님 께서 " + team.getTitle() + " 팀 맴버로 추가되었습니다.";
        List<ApplyResponseDto> applyResponseDtoList = getApplications(authentication, team.getId());

        //알람 생성
        String commentsAlarm = "🎉 축하합니다! " + team.getTitle() + "팀의 맴버가 되셨습니다!";
        alarmService.alarmMakerForLeader(commentsAlarm, applyInfo.getUser());

        return new ChoiceResponseDto(msg, applyInfo.getUser(), info, applyResponseDtoList);
    }


}
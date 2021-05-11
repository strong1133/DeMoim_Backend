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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    public ApplyResponseDto entityToDto(ApplyInfo applyInfo, ResponseUserDto responseUserDto) {
        ApplyResponseDto applyResponseDto = new ApplyResponseDto(applyInfo, responseUserDto);
        return applyResponseDto;
    }

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

    //팀메이킹 모집글 지원 _ 리더인가? 이미 신청한 사람인가? 참여중인프로젝트가 2개 이하인가? 자기 파트에 모집이 덜됐을 때
    public Map<String, String> applyTeam(Authentication authentication, Long teamId, ApplyRequestDto applyRequestDto) {

        System.out.println("================================================");

        Map<String, String> map = new HashMap<>();
        //User 정보 검증(from UserService.findCurUser)

        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()->new IllegalArgumentException("유효한 팀모집글 정보가 없습니다.")
        );

        ApplyInfo leaderInfo =  applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);
        System.out.println("leaderInfo : "+  leaderInfo);
//        Team team = teamService.findTeam(teamId);

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
//        } -> 이거 얘기 할것
        if (teamIdListOfUser.size() > 2) {
            throw new IllegalArgumentException("겹치는 프로젝트 기간 내에 참여할 수 있는 프로젝트는 최대 2개 입니다.");
        }
        System.out.println("6");
        //지원하려는 team의 신청자 포지션이 다 찼을때 지원 막기
        String userPosition = user.getPosition();
        System.out.println("userPosition : "+userPosition);
        List<Long> acceptedTeamMemberIdList = applyInfoRepository.findUserIdByTeamIdAndMembershipAndApplyState(teamId, ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED);
//        int numPositionApplied = 0;
        List<Long> samePositionMemberList = new ArrayList<Long>();
        for (Long acceptedTeamMemberId : acceptedTeamMemberIdList) {
            if (userPosition.equals(userRepository.findPositionById(acceptedTeamMemberId))) {
                samePositionMemberList.add(acceptedTeamMemberId);
            }
        }
        //팀메이킹 모집글의 해당 포지션 공고인원보다 확정된사람이 같거나 더 많은 경우
        int numPosition = checkPosition(userPosition, team);

        //0510 아침 영은님 요청사항 -> 자기포지션 모집인원0명일때는 별도의 메시지 내보내주기!
        if (numPosition == 0) {
            throw new IllegalArgumentException("해당 글은 회원님의 포지션을 모집하지 않습니다.");
        } else if (samePositionMemberList.size() >= numPosition) {
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
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()->new IllegalArgumentException("유효한 팀모집글 정보가 없습니다.")
        );

        ApplyInfo leaderInfo =  applyInfoRepository.findByTeamIdAndMembership(teamId, ApplyInfo.Membership.LEADER);

        System.out.println("leaderInfo의 userId"+leaderInfo.getUser().getId());
        System.out.println("user의 userId"+user.getId());

        if (user.equals(leaderInfo.getUser())) {
            //0510 영은님 요청(리더 제외한 팀원들 정보만!)
            List<ApplyInfo> applyInfoList = applyInfoRepository.findAllByTeamIdAndMembershipAndApplyState(teamId, ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.WAITING);
//            ResponseUserDto responseUserDto = new ResponseUserDto();
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
    public String cancelApplication(Authentication authentication, Long teamId) {
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        //Team 정보 검증
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()->new IllegalArgumentException("유효한 팀모집글 정보가 없습니다.")
        );

        //해당 지원을 취소할 수 있는 자격(멤버이면서 ACCEPTED인지) 확인 -> userId, teamId, membership = MEMBER, applyState=ACCEPTED인 상황에서 삭제 진행가능
        ApplyInfo userApplyInfo = applyInfoRepository.findByTeamIdAndUserId(teamId, user.getId());
        //지원부분(POST)은 equals 메소드로 동등성 비교를 함.
        if (userApplyInfo == null) {

            throw new IllegalArgumentException("팀 참여자 목록에서 회원님의 정보를 찾을 수 없습니다."); //얘 불필요한건지.. 어디서 걸러지나요?

        } else if (userApplyInfo.getMembership() != ApplyInfo.Membership.MEMBER) {

            throw new IllegalArgumentException("게시글 작성자는 본인의 글에 지원할 수 없습니다.");

        } else if (userApplyInfo.getApplyState() != ApplyInfo.ApplyState.ACCEPTED) {
            throw new IllegalArgumentException("해당 프로젝트의 참여확정된 사람에 한하여 취소가 가능합니다.");

        }

        LocalDateTime begin = team.getBegin();
        LocalDateTime end = team.getEnd();

        //해당 멤버가 지원한 모든 ApplyInfo 조회, 지금 삭제하는 프로젝트와 기간이 겹치는 프로젝트들에 대해 status를 DENIED->WAITING으로 바꿔주기
        List<ApplyInfo> memberApplyInfosList = applyInfoRepository.findAllByUserId(user.getId());

        for (ApplyInfo memberApplyInfo : memberApplyInfosList) {

            //해당 멤버가 지원한 프로젝트 하나하나마다 지금 취소하는 프로젝트와 겹치는게 있는 경우 명령문 실행
            if (!(memberApplyInfo.getTeam().getEnd().isBefore(begin) || memberApplyInfo.getTeam().getBegin().isAfter(end))) {

                memberApplyInfo.choiceMember(ApplyInfo.ApplyState.WAITING);
            }
        }
        applyInfoRepository.delete(userApplyInfo);
////        Team team = teamService.findTeam(teamId);
//
//        //해당 모집글에 대해 신청한사람들의 목록에 해당 유저의 정보가 있는지를 할기 위해 신청멤버 정보목록 생성
//        List<User> members = new ArrayList<User>();
//        for (ApplyInfo memberInfo : memberInfoList) {
//            members.add(memberInfo.getUser());
//        }
//
//        if (members.contains(user)) {
//            ApplyInfo applyInfo = applyInfoRepository.findByTeamIdAndUserId(teamId, user.getId());
//            applyInfoRepository.delete(applyInfo);
////            String success = "해당 게시물에 대한 지원취소가 완료되었습니다."
//            return "success";
//        } else {
////            "모집글에 대한 취소는 신청자만 할 수 있습니다.";
//            return "fail";
//        }

        return "해당 모집글에 대한 지원취소가 완료되었습니다.";
    }


    // 리더의 선택
    @Transactional
    public Map<String, String> choiceMember(Authentication authentication, Long applyId) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        ApplyInfo applyInfo = applyInfoRepository.findById(applyId).orElseThrow(
                () -> new IllegalArgumentException("해당 지원정보가 없습니다.")
        );
        Team team = teamService.findTeam(applyInfo.getTeam().getId());

        Long teamLeader = team.getLeader().getId();
        Long curUser = user.getId();
        if (!curUser.equals(teamLeader)) {
            throw new IllegalArgumentException("당신은 리더가 아닙니다.");
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //userId와 applyState로 해당 유저의 참여정보 조회(기간을 잘 따져야할것같은..? 안겹치는 기간이면 숫자가 많아도 상관없으니까!)
//        int memberJoinCnt = applyInfoRepository.countByUserIdAndApplyState(applyInfo.getUser().getId(), ApplyInfo.ApplyState.ACCEPTED);
//        System.out.println("user :" + user.getId());
//        System.out.println("MemberJoinCnt :" + memberJoinCnt);
        //프로젝트 기간을 비교하여 겹침 방지
//        if (memberJoinCnt > 1) {
//            throw new IllegalArgumentException("진행하시는 프로젝트는 서로 그 기간이 겹칠 수 없습니다.");
//        }

        String applyPosition = applyInfo.getUser().getPosition();
        List<Long> acceptedTeamMemberIdList = applyInfoRepository.findUserIdByTeamIdAndMembershipAndApplyState(team.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED);
        List<Long> samePositionMemberList = new ArrayList<Long>();


        int numPosition = checkPosition(applyPosition, team);

        for (Long acceptedTeamMemberId : acceptedTeamMemberIdList) {
            if (applyPosition.equals(userRepository.findPositionById(acceptedTeamMemberId))) {
                samePositionMemberList.add(acceptedTeamMemberId);
            }
        }
        if (samePositionMemberList.size() >= numPosition) {
            throw new IllegalArgumentException("해당 포지션의 인원이 가득찼습니다.");
        }
        //흐름 : 먼저 ACCEPTED 처리해주고나서 해당 지원자의 다른 지원정보를 싹 업데이트해주는 순서(서로겹칠만한 프로젝트는 여기서 찾아 DENIED로 바꿔주기
        applyInfo.choiceMember(ApplyInfo.ApplyState.ACCEPTED);
        //applyInfo.setApplyState(ApplyInfo.ApplyState.ACCEPTED); 일부러 Setter 방법 피한거겠죠?!
        LocalDateTime begin = applyInfo.getTeam().getBegin();
        LocalDateTime end = applyInfo.getTeam().getEnd();

        List<ApplyInfo> applyInfoOfApplicantList = applyInfoRepository.findAllByUserId(user.getId());
        for (ApplyInfo applyInfoOfApplicant : applyInfoOfApplicantList) {

            if (!(applyInfoOfApplicant.getTeam().getEnd().isBefore(begin) || applyInfoOfApplicant.getTeam().getBegin().isAfter(end))) {
                applyInfoOfApplicant.choiceMember(ApplyInfo.ApplyState.DENIED);
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("msg", applyInfo.getUser().getUsername() + "님이 팀원이 되셨습니다.");
        return map;
    }

}
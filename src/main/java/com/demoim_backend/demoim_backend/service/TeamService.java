package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import com.demoim_backend.demoim_backend.util.RandomImg;
import com.demoim_backend.demoim_backend.util.TeamJsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.ZoneOffset;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserService userService;
    private final TeamRepository teamRepository;
    private final FileUploadService fileUploadService;
    private final TeamJsonMapper teamJsonMapper;
    private final ApplyInfoRepository applyInfoRepository;
    private final RandomImg randomImg;


    //게시글 존재유무 검증
    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    public List<Team> getContentTeam(Page<Team> teamPage) {
        List<Team> teamList = teamPage.getContent();
        return teamList;
    }

    //팀메이킹 작성글 생성 _ auth 필요 (return type을 Dto로 내보내는게 맞을까? Team 아니고? -> 보안과 비용면에서 Dto로 내보내는게 맞다는 생각)
    public TeamResponseDto createTeam(Authentication authentication, String requestBody, MultipartFile file) {
        Random random = new Random();
        TeamRequestDto teamRequestDto;
        teamRequestDto = teamJsonMapper.jsonTeamDtoMaker(requestBody);
        if (file != null) {
            teamRequestDto.setThumbnail(fileUploadService.uploadImage(file));
        }
        if (file == null) {
            int rNum = random.nextInt(15);
            teamRequestDto.setThumbnail(randomImg.rndImg(rNum));
        }
        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        LocalDateTime recruitLDT = Instant.ofEpochMilli(teamRequestDto.getRecruit()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime beginLDT = Instant.ofEpochMilli(teamRequestDto.getBegin()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime endLDT = Instant.ofEpochMilli(teamRequestDto.getEnd()).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

//        Date localRecruit = teamRequestDto.getRecruit().
        UserUpdateProfileSaveRequestDto userUpdateProfileSaveRequestDto = new UserUpdateProfileSaveRequestDto(user);
        TeamResponseDto teamResponseDto = new TeamResponseDto(teamRequestDto, userUpdateProfileSaveRequestDto);


        //team 작성글 존재여부 검증(from TeamService.findTeam)

        Team team = Team.createTeam(teamRequestDto, user);
        teamRepository.save(team);
        teamResponseDto.setTeamId(team.getId());
        teamResponseDto.setCreatedAt(team.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());


        System.out.println("createdAt의 LocalDateTime : " + team.getCreatedAt());
        System.out.println("밀리초 변환 1안(아마 초변환인듯) : " + team.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        System.out.println("밀리초 변환 1안(아마 초변환인듯) : " + Long.toString(team.getCreatedAt().toEpochSecond(ZoneOffset.UTC)).length());

//        Long nowtime = team.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
//        String len = Long.toString(nowtime);

        System.out.println("밀리초 변환 2안 : " + team.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());
        System.out.println("밀리초 변환 2안 길이 : " + Long.toString(team.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()).length());


        System.out.println("now : " + System.currentTimeMillis());
        System.out.println("now 길이 : " + Long.toString(System.currentTimeMillis()).length());

        //TeamUserInfo에 리더 정보 저장
        ApplyResponseSaveDto applyResponseSaveDto = new ApplyResponseSaveDto(team);
        ApplyInfo leaderInfo = ApplyInfo.createTeamUserInfo(applyResponseSaveDto, user);
        applyInfoRepository.save(leaderInfo);

        return teamResponseDto;
    }

    //팀메이킹 작성글 썸네일 업로드 _ auth 필요
    public String createTeamImg(Authentication authentication, MultipartFile file) {

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("회원 정보가 존재하지 않습니다.")
        );

        String thumbnail = fileUploadService.uploadImage(file);

        return thumbnail;
    }


//    public Page<Team> getAllTeams(Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1); // page 는 index 처럼 0 부터 시작
//        pageable = PageRequest.of(page, 10);
//        return teamRepository.findAll(pageable);
//    }

    //팀메이킹 작성글 전체조회 with 페이지네이션 _ auth 불필요
    public List<TeamResponseDto> getTeamList(int page, int size) { //(int page, int size, String sortBy, boolean isAsc)

        Page<Team> pageTeam = teamRepository.findAll(PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt"));

        //페이징한 Page<Team>를 List<TeamResponseDto>에 담기
//        List<Team> listTeam = pageTeam.getContent(); // toList 메소드와 getContent 차이는?

        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (Team team : getContentTeam(pageTeam)) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }

        return teamResponseDtoList;
    }


    //특정 팀메이킹 작성글 조회(작성글 id로) _ auth 불필요
    public TeamResponseDto getTeam(Long teamId) {

        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = this.findTeam(teamId);

        UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(team.getLeader());
        TeamResponseDto teamResponseDto = new TeamResponseDto(team, leaderProfile);


        return teamResponseDto;
    }

    //팀메이킹 작성글 수정 _ auth 필요
    @Transactional
    public TeamResponseDto update(Authentication authentication, Long teamId, String requestBody, MultipartFile file) {
        TeamRequestDto teamRequestDto;
        teamRequestDto = teamJsonMapper.jsonTeamDtoMaker(requestBody);
        TeamResponseDto teamResponseDto;

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
        UserUpdateProfileSaveRequestDto leaderProfileDto = new UserUpdateProfileSaveRequestDto(user);

        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = this.findTeam(teamId);

        if (file == null) {
            teamRequestDto.setThumbnail(team.getThumbnail());
        } else {
            teamRequestDto.setThumbnail(fileUploadService.uploadImage(file));
        }

        //authentication 통해 뽑은 User와 Team메이킹 글의 leader가 같은지 여부 체크
        if (user.getId() == team.getLeader().getId()) {
            team.update(teamRequestDto);
            teamResponseDto = new TeamResponseDto(team, leaderProfileDto);
        } else {
            teamResponseDto = null;
        }
        return teamResponseDto;
    }


    //팀메이킹 작성글 날짜별 recruitState & projectState 업데이트
    @Transactional
    public void updateState(Long teamId, LocalDateTime now) {
        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = this.findTeam(teamId);

        TeamStateUpdateResponseDto teamStateDto = new TeamStateUpdateResponseDto();


//        Long teamId = team.getId();

        Team.StateNow recruitState = team.getRecruitState();
        Team.StateNow projectState = team.getProjectState();
        //지금 시간 기준으로 recruitState 값 재설정
//        if (now.after(team.getRecruit()) || now.equals(team.getRecruit())) {
        if (now.compareTo(team.getRecruit()) >= 0) {
            System.out.println("모집상태 변경 ACTIVATED -> FINISHED");
            recruitState = Team.StateNow.FINISHED;
        }
        //지금 시간 기준으로 projectState 값 재설정
//        if (now.after(team.getBegin()) && now.before(team.getEnd())) {
        if (now.compareTo(team.getBegin()) >= 0 && now.compareTo(team.getEnd()) < 0) {
//                team.setProjectState(Team.StateNow.ACTIVATED);
            System.out.println("프로젝트상태 변경 YET -> ACTIVATED");
            projectState = Team.StateNow.ACTIVATED;
//        } else if (now.after(team.getEnd()) || now.equals(team.getEnd())) {
        } else if (now.compareTo(team.getEnd()) >= 0) {
//                team.setProjectState(Team.StateNow.FINISHED);
            System.out.println("프로젝트상태 변경 ACTIVATED -> FINISHED");
            projectState = Team.StateNow.FINISHED;
        }
        teamStateDto.setRecruitState(recruitState);
        teamStateDto.setProjectState(projectState);

        team.updateByTeamStateUpdateResponseDto(teamStateDto);


    }

    //팀메이킹 작성글 삭제 _ auth 필요
    public String deleteTeam(Authentication authentication, Long teamId) {

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = this.findTeam(teamId);

        if (user.getId() == team.getLeader().getId()) {
            teamRepository.deleteById(teamId);
            return "삭제 성공";
        } else {
            return "게시글 작성자가 아닙니다.";
        }
    }

    // Front 필터링
    public List<TeamResponseDto> findTeamWhereFront(int page, int size) {
        Page<Team> teamPage = teamRepository.findAllByFrontGreaterThan(0, PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt"));
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (Team team : getContentTeam(teamPage)) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }
        return teamResponseDtoList;
    }

    // Back 필터링
    public List<TeamResponseDto> findTeamWhereBack(int page, int size) {
        Page<Team> teamPage = teamRepository.findAllByBackGreaterThan(0, PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt"));
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (Team team : getContentTeam(teamPage)) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }
        return teamResponseDtoList;
    }

    // Designer 필터링
    public List<TeamResponseDto> findTeamWhereDesigner(int page, int size) {
        Page<Team> teamPage = teamRepository.findAllByDesignerGreaterThan(0, PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt"));
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (Team team : getContentTeam(teamPage)) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }
        return teamResponseDtoList;
    }

    // Planner 필터링
    public List<TeamResponseDto> findTeamWherePlanner(int page, int size) {
        Page<Team> teamPage = teamRepository.findAllByPlannerGreaterThan(0, PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt"));
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<>();

        for (Team team : getContentTeam(teamPage)) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }
        return teamResponseDtoList;
    }
}


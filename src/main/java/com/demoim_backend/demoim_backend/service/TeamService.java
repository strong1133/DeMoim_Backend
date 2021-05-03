package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.dto.TeamResponseDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileSaveRequestDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
//    private final TeamResponseDto teamResponseDto;

//    //Authentication 통해 객체 정보존재여부 조회
//    public User getUser(Authentication authentication) {
//        System.out.println("getUser() _ 1");
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        System.out.println("getUser() _ 2");
//
//        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
//                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
//        );
//        System.out.println("getUser() _ 3");
//
//        return user;
//    }

    //팀메이킹 작성글 생성 _ auth 필요 (return type을 Dto로 내보내는게 맞을까? Team 아니고?)
    public TeamResponseDto createTeam(Authentication authentication,TeamRequestDto teamRequestDto) {

        System.out.println("TeamService 진입 후");
        //여기서의 this는 객체 내의 getUser 메소드를 가져오기 위해 표기한 것
//        User user = this.getUser(authentication);
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        System.out.println("가져온 User 정보 : "+user);
        UserUpdateProfileSaveRequestDto userUpdateProfileSaveRequestDto = new UserUpdateProfileSaveRequestDto(user);
        System.out.println("userUpdateProfileSaveRequestDto 확인");
        System.out.println(userUpdateProfileSaveRequestDto);
        TeamResponseDto teamResponseDto = new TeamResponseDto(teamRequestDto, userUpdateProfileSaveRequestDto);
        System.out.println("teamRequestDto 확인");
        System.out.println(teamResponseDto);
        //저장할 Team객체 생성
        Team team = Team.createTeam(teamRequestDto, user);
        System.out.println("team 확인");
        System.out.println(team);
        teamRepository.save(team);
        //
        return teamResponseDto;
    }



//    public Page<Team> getAllTeams(Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1); // page 는 index 처럼 0 부터 시작
//        pageable = PageRequest.of(page, 10);
//        return teamRepository.findAll(pageable);
//    }

    //팀메이킹 작성글 전체조회 with 페이지네이션 _ auth 불필요
    public List<TeamResponseDto> getTeamList(int page, int size) { //(int page, int size, String sortBy, boolean isAsc)

        Page<Team> pageTeam = teamRepository.findAll(PageRequest.of(page-1, size,Sort.Direction.DESC,"modifiedAt"));

        //페이징한 Page<Team>를 List<TeamResponseDto>에 담기
        List<Team> listTeam = pageTeam.getContent(); // toList 메소드와 getContent 차이는?
        List<TeamResponseDto> teamResponseDtoList = new ArrayList<TeamResponseDto>();

        for (Team team : listTeam) {
            User leader = team.getLeader();
            UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(leader);
            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
            teamResponseDtoList.add(responseDto);
        }

        return teamResponseDtoList;
    }
//    public Page<Team> getAllTeams(int page, int size) { //(int page, int size, String sortBy, boolean isAsc)
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size); //(page, size, sort)

        //팀메이킹 글 전체조회시에도 User객체 그대로 보내준다면 불필요한 정보 + password까지 포함시켜 클라이언트에 응답하게 되는데, 이를 방지할수는 없을까? Dto를 만들어서 collection으로 만든다 하더라도 결국 Entity처럼 사용할 수 없는 한계에 부딪침(JPARepository 사용을 못함)
//        List<Team> teams = teamRepository.findAllByOrderByModifiedAtDesc();
//        List<TeamResponseDto> teamResponseDtos = new ArrayList<TeamResponseDto>();
//        for (Team team:teams) {
//            User leader = team.getLeader();
//            UserUpdateProfileSaveDto leaderProfile = new UserUpdateProfileSaveDto(leader);
//            TeamResponseDto responseDto = new TeamResponseDto(team, leaderProfile);
//            teamResponseDtos.add(responseDto);
//        }
//        Page<TeamResponseDto> pagination = teamResponseDtos(PageRequest.of(page-1, size));


    //특정 팀메이킹 작성글 조회(작성글 id로) _ auth 불필요
    public TeamResponseDto getTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다.")
        );

        UserUpdateProfileSaveRequestDto leaderProfile = new UserUpdateProfileSaveRequestDto(team.getLeader());
        TeamResponseDto teamResponseDto = new TeamResponseDto(team, leaderProfile);


        return teamResponseDto;
    }
//    public Team getTeam(Long teamId) {
//        Team team = teamRepository.findById(teamId).orElseThrow(
//                () -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다.")
//        );
//
//        return team;
//    }

    //팀메이킹 작성글 수정 _ auth 필요
    @Transactional
    public TeamResponseDto update(Authentication authentication, Long teamId, TeamRequestDto teamRequestDto) {

        TeamResponseDto teamResponseDto;

//        User user = this.getUser(authentication);
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        UserUpdateProfileSaveRequestDto leaderProfileDto = new UserUpdateProfileSaveRequestDto(user);

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new IllegalArgumentException("해당 모집글이 존재하지 않습니다.")
        );

        //authentication 통해 뽑은 User와 Team메이킹 글의 leader가 같은지 여부 체크
        if (user.getId() == team.getLeader().getId()) {
            team.update(teamRequestDto);
            teamResponseDto = new TeamResponseDto(teamRequestDto, leaderProfileDto);
        } else {
            teamResponseDto = null;
        }
        return teamResponseDto;
    }

    //팀메이킹 작성글 삭제 _ auth 필요
    public String deleteTeam(Authentication authentication, Long teamId) {

//        User user = this.getUser(authentication);

        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new IllegalArgumentException("해당 모집글이 존재하지 않습니다.")
        );

        if (user.getId() == team.getLeader().getId()) {
            teamRepository.deleteById(teamId);
            return "삭제 성공";
        } else {
            return "게시글 작성자가 아닙니다.";
        }
    }
}

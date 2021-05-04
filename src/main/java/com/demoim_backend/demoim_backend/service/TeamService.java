package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.dto.TeamResponseDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileSaveRequestDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final UserService userService;
    private final TeamRepository teamRepository;
    private final FileUploadService fileUploadService;

    //게시글 존재유무 검증
    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    //팀메이킹 작성글 생성 _ auth 필요 (return type을 Dto로 내보내는게 맞을까? Team 아니고? -> 보안과 비용면에서 Dto로 내보내는게 맞다는 생각)
    public TeamResponseDto createTeam(Authentication authentication,TeamRequestDto teamRequestDto) {

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        UserUpdateProfileSaveRequestDto userUpdateProfileSaveRequestDto = new UserUpdateProfileSaveRequestDto(user);
        TeamResponseDto teamResponseDto = new TeamResponseDto(teamRequestDto, userUpdateProfileSaveRequestDto);

        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = Team.createTeam(teamRequestDto, user);
        teamRepository.save(team);
        teamResponseDto.setTeamId(team.getId());
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
    public TeamResponseDto update(Authentication authentication, Long teamId, TeamRequestDto teamRequestDto) {

        TeamResponseDto teamResponseDto;

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );

        UserUpdateProfileSaveRequestDto leaderProfileDto = new UserUpdateProfileSaveRequestDto(user);

        //team 작성글 존재여부 검증(from TeamService.findTeam)
        Team team = this.findTeam(teamId);

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

        //User 정보 검증(from UserService.findCurUser)
        User user = userService.findCurUser(authentication).orElseThrow(
                ()-> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
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
}

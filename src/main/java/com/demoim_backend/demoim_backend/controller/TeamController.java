package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.dto.TeamResponseDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TeamController {

    //private final ProjectRepository projectRepository;
    private final TeamService teamService;

    //팀 게시물 작성 _ Post _ /api/team _ auth 필요
    @PostMapping("/api/team")
    public TeamResponseDto createTeam(Authentication authentication,@RequestPart(value ="requestBody" ) String requestBody,
                                      @RequestPart(required = false) MultipartFile file) {

        System.out.println("TeamService 진입 전");
        return teamService.createTeam(authentication, requestBody,file);
    }

    //팀 게시물의 첨부이미지 -> url 반환되니 upload api로 대체 가능
    @PostMapping("/api/TeamImg")
    public String createTeamImg(Authentication authentication, @RequestPart MultipartFile file) {
        return teamService.createTeamImg(authentication, file);
    }

    //팀 게시물 읽기 _ Get _ /api/team?page={page_num}&size={size_num} _ auth 불필요
    @GetMapping("/api/team") // 페이지네이션에 관해서 페이지넘버와 사이즈넘버는 repository에서 처리?
    public List<TeamResponseDto> getTeamList(@RequestParam("page") int page, @RequestParam("size") int size) {
        return teamService.getTeamList(page, size);
    }


    //특정 팀 게시물 상세보기 _ Get _ /api/team/detail?team_id={team_id} _ auth 불필요
    @GetMapping("/api/team/detail")
    public TeamResponseDto readTeam(@RequestParam("team_id") Long teamId) {

        return teamService.getTeam(teamId);
    }

    //특정 팀 게시물 수정 _ Put _ /api/team/detail?team_id={team_id} _ auth 필요
    @PutMapping("/api/team/detail")
    public TeamResponseDto editTeam(Authentication authentication, @RequestParam("team_id") Long teamId, @RequestBody TeamRequestDto teamRequestDto) {

        TeamResponseDto team =  teamService.update(authentication, teamId, teamRequestDto);

        if (team == null) {
            throw new IllegalArgumentException("게시글의 작성자가 아닙니다.");
        } else {
            team.setTeamId(teamId);
            return team;
        }
    }
    //특정 팀 게시물 삭제 _ Delete _ /api/team/detail?team_id={team_id} _ auth 필요
    @DeleteMapping("/api/team/detail")
    public ResponseEntity deleteTeam(Authentication authentication, @RequestParam("team_id") Long teamId) {
        //해당 게시물에 관련된 댓글도 나중에 지워주는 코드 삽입해야하지 않을까??
        String result = teamService.deleteTeam(authentication, teamId);
        HashMap<String, String> message;

        if (result.equals("삭제 성공")) {
            message = new HashMap<>();
            message.put("msg",result);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message = new HashMap<>();
            message.put("msg", result);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }


}

package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TeamController {

    //    private final ProjectRepository projectRepository;
    private final TeamService teamService;

    //    팀 게시물 작성 _ Post _ /api/team
    @PostMapping("/api/team")
    public Team writeTeam(@RequestBody TeamRequestDto teamRequestDto) {

        Team team = teamService.saveTeam(teamRequestDto);
        return team;
    }

    //    팀 게시물 읽기 _ Get _ /api/team?page={page_num}&size={size_num}
    @GetMapping("/api/team") // 페이지네이션에 관해서 페이지넘버와 사이즈넘버는 repository에서 처리?
    public Page<Team> getAllTeams(@RequestParam("page") int page, @RequestParam("size") int size) {
        return teamService.getAllTeams(page, size);
    }

//            @RequestParam("page_num") int page_num,
//            @RequestParam("size_num") int size_num
//            /*,@RequestParam("sortBy") String sortBy,
//            @RequestParam("isAsc") boolean isAsc*/) {
//
//        return teamService.getAllTeams(page_num, size_num); //, sortBy, isAsc
//    }

    //    특정 팀 게시물 상세보기 _ Get _ /api/team/detail?team_id={team_id}
    @GetMapping("/api/team/detail")
    public Team readTeam(@RequestParam("team_id") Long teamId) {

        return teamService.getTeam(teamId);
    }

    //    특정 팀 게시물 수정 _ Put _ /api/team/detail?team_id={team_id}
    @PutMapping("/api/team/detail")
    public Team editTeam(@RequestParam("team_id") Long teamId, @RequestBody TeamRequestDto teamRequestDto) {

        return teamService.update(teamId, teamRequestDto);
    }
    //    특정 팀 게시물 삭제 _ Delete _ /api/team/detail?team_id={team_id}
    @DeleteMapping("/api/team/detail")
    public String deleteTeam(@RequestParam("team_id") Long teamId) {
        //해당 게시물에 관련된 댓글도 나중에 지워주는 코드 삽입해야!
        return teamService.deleteTeam(teamId);
    }


}

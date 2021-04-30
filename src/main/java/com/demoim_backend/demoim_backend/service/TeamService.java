package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    //팀메이킹 작성글 저장
    public Team saveTeam(TeamRequestDto teamRequestDto) {
        Team team = new Team(teamRequestDto);
        teamRepository.save(team);
        return team;
//        return teamRepository.save(team);
    }

//    public Page<Team> getAllTeams(Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1); // page 는 index 처럼 0 부터 시작
//        pageable = PageRequest.of(page, 10);
//        return teamRepository.findAll(pageable);
//    }

    //팀메이킹 작성글 전체조회 with 페이지네이션
    public Page<Team> getAllTeams(int page, int size) { //(int page, int size, String sortBy, boolean isAsc)
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size); //(page, size, sort)
        Page<Team> pagination = teamRepository.findAll(PageRequest.of(page-1, size,Sort.Direction.DESC,"modifiedAt"));
        return pagination;
    }

    //특정 팀메이킹 작성글 조회(작성글 id로)
    public Team getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("해당 모집글이 존재하지 않습니다.")
        );
        return team;
    }

    //팀메이킹 작성글 수정
    public Team update(Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        team.update(teamRequestDto);
        return team;
    }

    //팀메이킹 작성글 삭제
    public String deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
        return "삭제 성공";
    }
}

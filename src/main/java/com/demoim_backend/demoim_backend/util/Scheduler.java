package com.demoim_backend.demoim_backend.util;

import com.demoim_backend.demoim_backend.controller.TeamController;
import com.demoim_backend.demoim_backend.dto.TeamStateUpdateResponseDto;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InterruptedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Scheduler {

    //정해진 시간 기준으로 DB의 stateRecruit, stateProject 값을 변경
    private final TeamService teamService;
    private final TeamController teamController;
    private final TeamRepository teamRepository;
//    private final LocalDateTime localDateTime;

    //초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 0 * * *")
    private void updateStates() throws InterruptedIOException {
        System.out.println("팀메이킹 객체 별 모집상황, 프로젝트상황 업데이트");
        List<Team> teamList = teamRepository.findAll();
//        Date now = new Date(System.currentTimeMillis());
//        Date now = new Date();
        LocalDateTime now = LocalDateTime.now();
        for (Team team: teamList) {
            Long teamId = team.getId();
            //업데이트된 recruitState와 projectState 저장
            teamService.updateState(teamId, now);
        }
    }
}

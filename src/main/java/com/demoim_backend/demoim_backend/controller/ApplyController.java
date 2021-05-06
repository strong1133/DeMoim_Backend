package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.ApplyRequestDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.ApplyService;
import com.demoim_backend.demoim_backend.service.TeamService;
import com.demoim_backend.demoim_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApplyController {
    /*
    지원하기, 지원취소, 리더입장에서 지원자 조회
     */
//    private final Authentication authentication;
//    private final UserService userService;
//    private final TeamService teamService;
    private final ApplyService applyService;

    @PostMapping("/api/apply") // /api/apply?team_id={team_id}
    public ApplyResponseDto applyTeam(Authentication authentication, @RequestParam("team_id") Long teamId, @RequestBody ApplyRequestDto applyRequestDto) {

        ApplyResponseDto applyResponseDto = applyService.applyTeam(authentication, teamId, applyRequestDto);

        return applyResponseDto;
    }

    @GetMapping("/api/apply") // /api/apply?team_id={team_id}
    public List<ApplyResponseDto> getApplications(Authentication authentication, @RequestParam("team_id") Long teamId) {
        List<ApplyResponseDto> applyResponseDtoList = applyService.getApplications(authentication, teamId);

        if(applyResponseDtoList == null){
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }else{
            return applyResponseDtoList;
        }
    }


    @DeleteMapping("/api/apply") // /api/apply?apply_id={apply_id}
    public void cancelApplication(Authentication authentication, @RequestParam("team_id") Long teamId) {
        applyService.cancelApplication(authentication, teamId);

    }
}

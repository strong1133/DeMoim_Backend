package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.ApplyRequestDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseSaveDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
import com.demoim_backend.demoim_backend.dto.ChoiceResponseDto;
import com.demoim_backend.demoim_backend.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ApplyController {
    /*
    지원하기, 지원취소, 리더입장에서 지원자 조회
     */
//    private final Authentication authentication;
//    private final UserService userService;
//    private final TeamService teamService;
    @Autowired
    private final ApplyService applyService;

    @PostMapping("/api/apply") // /api/apply?team_id={team_id}
    public Map<String, String> applyTeam(Authentication authentication, @RequestParam("team_id") Long teamId, @RequestBody ApplyRequestDto applyRequestDto) {
        return applyService.applyTeam(authentication, teamId, applyRequestDto);

    }

    @GetMapping("/api/apply") // /api/apply?team_id={team_id}
    public List<ApplyResponseDto> getApplications(Authentication authentication, @RequestParam("team_id") Long teamId) {
        List<ApplyResponseDto> applyResponseDtoList = applyService.getApplications(authentication, teamId);
        return applyResponseDtoList;
    }

    @DeleteMapping("/api/apply") // /api/apply?apply_id={apply_id}
    public Map<String, String> cancelApplication(Authentication authentication, @RequestParam("team_id") Long teamId) {
        return applyService.cancelApplication(authentication, teamId);
    }

    @PutMapping("/api/apply/choice")
    public ChoiceResponseDto choiceMember(Authentication authentication, @RequestParam("apply_id") Long applyId) {
        return applyService.choiceMember(authentication, applyId);
    }
}

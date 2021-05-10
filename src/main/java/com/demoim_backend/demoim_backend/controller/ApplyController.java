package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.ApplyRequestDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseSaveDto;
import com.demoim_backend.demoim_backend.dto.ApplyResponseDto;
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
    public List<ApplyResponseDto>  getApplications(Authentication authentication, @RequestParam("team_id") Long teamId) {
        List<ApplyResponseDto>  applyResponseDtoList = applyService.getApplications(authentication, teamId);
        return applyResponseDtoList;
    }


    @DeleteMapping("/api/apply") // /api/apply?apply_id={apply_id}
    public ResponseEntity cancelApplication(Authentication authentication, @RequestParam("team_id") Long teamId) {

        String result = applyService.cancelApplication(authentication, teamId);
        HashMap<String, String> message;

        if(result.equals("success")){
            message = new HashMap<>();
            message.put("msg", "해당 게시물에 대한 지원취소가 완료되었습니다.");
            return new ResponseEntity<>(message, HttpStatus.OK);
//            applyService.cancelApplication(authentication, teamId);
        }else{
            message = new HashMap<>();
            message.put("msg", "모집글에 대한 취소는 신청자만 할 수 있습니다.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        }


    }
}

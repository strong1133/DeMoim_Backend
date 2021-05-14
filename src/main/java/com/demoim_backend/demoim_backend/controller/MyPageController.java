package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MypageService mypageService;
    private final UserService userService;

    // 현재 로그인 유저가 작성한 smalltalk
    @GetMapping("/api/mypage/smalltalk")
    public List<SmallTalkResponseDto> findMySmallTalk(@Valid Authentication authentication, @RequestParam(required = false, name = "user_id") Long userId) {
        if (userId==null){
            User user = userService.findMyUserInfo(authentication);
            return mypageService.findMySmallTalk(user.getId());
        }else{
            return mypageService.findMySmallTalk(userId);
        }
    }

    @GetMapping("/api/mypage/exhibition")
    public List<ExhibitionResponseDto> findMyExhibition(@Valid Authentication authentication, @RequestParam(required = false, name = "user_id") Long userId) {
        if (userId==null){
            User user = userService.findMyUserInfo(authentication);
            return mypageService.findMyExhibition(user.getId());
        }else{
            return mypageService.findMyExhibition(userId);
        }
    }

    @GetMapping("/api/mypage/apply")
    public List<TeamResponseDto> findMyApply(@Valid Authentication authentication, @RequestParam(required = false, name = "user_id") Long userId) {
        if (userId==null){
            User user = userService.findMyUserInfo(authentication);
            return mypageService.findMyApply(user.getId());
        }else{
            return mypageService.findMyApply(userId);
        }
    }

    @GetMapping("/api/mypage/leader")
    public List<ActiveTeamResponseDto> myTeamAsLeader(@Valid Authentication authentication, @RequestParam(required = false, name = "user_id") Long userId) throws NoSuchFieldException {
        if (userId==null){
            User user = userService.findMyUserInfo(authentication);
            return mypageService.findMyTeamAsLeader(user.getId());
        }else{
            return mypageService.findMyTeamAsLeader(userId);
        }
    }

    //마이페이지 중 프로젝트 히스토리(내가 지원한 프로젝트 / 내가 진행중, 참여한 프로젝트 / 내가 리더인 프로젝트)
    @GetMapping("/api/mypage/team")
    public Map<String, Object> findMyActiveTeam(@Valid Authentication authentication, @RequestParam(required = false, name = "user_id") Long userId) {
        Map<String, Object> myJoinedTeam;
        if (userId==null){
            User user = userService.findMyUserInfo(authentication);
            myJoinedTeam = mypageService.findMyActivedTeam(user.getId());
        }else{
            myJoinedTeam = mypageService.findMyActivedTeam(userId);
        }
        Map<String, Object> projectHistory = new HashMap<>();
        projectHistory.put("myTeamHistory", myJoinedTeam);
        return projectHistory;
    }
}

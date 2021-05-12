package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.service.ApplyService;
import com.demoim_backend.demoim_backend.service.ExhibitionService;
import com.demoim_backend.demoim_backend.service.MypageService;
import com.demoim_backend.demoim_backend.service.SmallTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MypageService mypageService;

    // 현재 로그인 유저가 작성한 smalltalk
    @GetMapping("/api/mypage/smalltalk")
    public List<SmallTalkResponseDto> findMySmallTalk(Authentication authentication){
        return mypageService.findMySmallTalk(authentication);
    }

    @GetMapping("/api/mypage/exhibition")
    public List<ExhibitionResponseDto> findMyExhibition(Authentication authentication){
        return mypageService.findMyExhibition(authentication);
    }

    @GetMapping("/api/mypage/apply")
    public List<TeamResponseDto> findMyApply(Authentication authentication){
        return mypageService.findMyApply(authentication);
    }

    @GetMapping("/api/mypage/leader")
    public ActiveTeamResponseDto myTeamAsLeader (Authentication authentication) throws NoSuchFieldException {
        return mypageService.findMyTeamAsLeader(authentication);
    }
    //마이페이지 중 프로젝트 히스토리(내가 지원한 프로젝트 / 내가 진행중, 참여한 프로젝트 / 내가 리더인 프로젝트)
    @GetMapping("/api/mypage/team")
    public MypageHistoryResponseDto findMyActiveTeam(Authentication authentication){
       return mypageService.findMyHistoryTeam(authentication);
//        return mypageService.findMyActiveTeam(authentication);
    }

}

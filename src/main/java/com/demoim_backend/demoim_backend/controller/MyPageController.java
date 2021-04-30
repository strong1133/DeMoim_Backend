package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.service.SmallTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final SmallTalkService smallTalkService;

    // 현재 로그인 유저가 작성한 smalltalk
    @GetMapping("/api/mypage/smalltalk")
    public List<SmallTalkResponseDto> findMySmallTalk(Authentication authentication){
        return smallTalkService.findMySmallTalk(authentication);
    }
}

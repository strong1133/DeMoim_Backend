package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/api/signup")
    public User signupUser(@RequestBody SignupRequestDto signupRequestDto){
        return signupService.signupUser(signupRequestDto);
    }

    @PostMapping("/api/signup/usernamedupchk")
    private Map<String, String> duplicateChkUsername(@RequestParam String username){
        return signupService.duplicateChkUsername(username);
    }

    @PostMapping("/api/signup/nicknamedupchk")
    private Map<String, String> duplicateChkNickname(@RequestParam String nickname){
        return signupService.duplicateChkNickname(nickname);
    }
}

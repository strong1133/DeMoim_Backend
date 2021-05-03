package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileRequestDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.UserService;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @GetMapping("/api/login")
//    private Optional<User>  loginUser(Authentication authentication){
//        return findCurUser(authentication);
//    }
    // 현재 로그인 유저 정보 반환
    @GetMapping("/api/mypage/profile")
    private Optional<User> findCurUser(Authentication authentication){
        return userService.findCurUser(authentication);
    }

    // 특정 유저 정보 반환
    @GetMapping("/api/user/profile")
    private User findTargetUser(@RequestParam(value = "user_id") Long userId){
        return userService.findTargetUser(userId);
    }

    // 유저 정보 수정
    @PutMapping("/api/mypage/profile")
    private User updateProfile(Authentication authentication, @RequestBody UserUpdateProfileRequestDto userUpdateProfileRequestDto, @RequestPart(required = false) MultipartFile file){
        return userService.updateProfile(authentication,userUpdateProfileRequestDto,file);
    }

    // 유저 정보 수정
    @PutMapping("/api/mypage/profileImg")
    private User updateProfileImg(Authentication authentication, @RequestPart MultipartFile file){
        return userService.updateProfileImg(authentication,file);
    }

}

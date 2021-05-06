package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.UserUpdateProfileRequestDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
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
    private User updateProfile(Authentication authentication, @RequestPart("userEditInfo")String userEditInfo , @RequestPart(required = false) MultipartFile file){
        System.out.println("userEditInfo : " + userEditInfo);
        System.out.println("file : " + file);

        return userService.updateProfile(authentication,userEditInfo,file);
    }

    // 유저 정보 수정
    @PutMapping("/api/mypage/profileImg")
    private User updateProfileImg(Authentication authentication, @RequestPart MultipartFile file){
        return userService.updateProfileImg(authentication,file);
    }

}

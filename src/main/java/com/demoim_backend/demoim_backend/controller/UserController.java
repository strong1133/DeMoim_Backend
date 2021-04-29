package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/cur_user")
    private Optional<User> findCurUser(Authentication authentication){
        return userService.findCurUser(authentication);
    }



}

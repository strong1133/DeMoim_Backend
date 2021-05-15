package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.dto.CheckNumCertNumReuqestDto;
import com.demoim_backend.demoim_backend.dto.CheckNumRequestDto;
import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.service.SignupService;
import com.demoim_backend.demoim_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;
    private final UserService userService;

    @PostMapping("/api/signup")
    public User signupUser(@RequestBody SignupRequestDto signupRequestDto){
        return signupService.signupUser(signupRequestDto);
    }

    @PostMapping("/api/signup/usernamedupchk")
    private Map<String, String> duplicateChkUsername(@RequestParam String username){

        return signupService.duplicateChkUsername(username);
    }

    @PostMapping("/api/signup/nicknamedupchk")
    private Map<String, String> duplicateChkNickname(@Valid Authentication authentication, @RequestParam String nickname){
        Map<String, String> map = new HashMap<>();
        if (!(authentication==null)){
            User user = userService.findCurUser(authentication).orElseThrow(
                    ()-> new IllegalArgumentException("해당 유저가 없습니다.")
            );
            System.out.println(user.getNickname());
            if (user.getNickname().equals(nickname)){
                map.put("msg","true");
                return map;
            }
        }
        return signupService.duplicateChkNickname(nickname);
    }

    // certNumber 전송
    @PostMapping("/api/signup/certNumber")
    private Map<String,String> sendCertNumber(@RequestBody @Valid CheckNumRequestDto checkNumRequestDto){
        return signupService.sendCertNumber(checkNumRequestDto.getUsername(),checkNumRequestDto.getPhoneNum());
    }

    // certNumber 확인
    @GetMapping("/api/signup/certNumber")
    private Map<String,String> duplicatePhoneCheck(@RequestBody @Valid CheckNumCertNumReuqestDto checkNumCertNumReuqestDto){
        return signupService.duplicatePhoneCheck(checkNumCertNumReuqestDto.getUsername(), checkNumCertNumReuqestDto.getCertNumber());
    }

    @GetMapping("/api/c")
    public void dd(){
        signupService.get();
    }
}

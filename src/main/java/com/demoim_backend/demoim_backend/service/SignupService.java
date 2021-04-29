package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.SignupRepository;
import com.demoim_backend.demoim_backend.util.DuplicateChecker;
import com.demoim_backend.demoim_backend.util.SignupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SignupService {
    private final PasswordEncoder passwordEncoder;
    private final SignupRepository signupRepository;
    private final DuplicateChecker duplicateChecker;
    private static final String SCERET_KEY = "AWDSDV+/asdwzwr3434@#$vvadflf00ood/[das";

    //중복체크_이메일
    public Map<String, String> duplicateChkUsername(String username) {
        Map<String, String> map = new HashMap<>();
        if (!SignupValidator.usernameValid(username)){
            throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
        }
        if (!duplicateChecker.duplicateChkUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
        map.put("msg", "사용 가능한 이메일 입니다.");
        return map;
    }

    //중복체크_닉네임
    public Map<String, String> duplicateChkNickname(String nickname) {
        Map<String, String> map = new HashMap<>();
        if (!duplicateChecker.duplicateChkNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
        map.put("msg", "사용 가능한 닉네임 입니다.");
        return map;
    }

    //회원가입
    public User signupUser(SignupRequestDto signupRequestDto) {

        // username 유효성 검사
        String username = signupRequestDto.getUsername();
        String nickname = signupRequestDto.getNickname();
        if (!SignupValidator.usernameValid(username)){
            throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
        }
        if (!duplicateChecker.duplicateChkUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
        if (!duplicateChecker.duplicateChkNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }

        // password 유효성 검사
        String lawPassword = signupRequestDto.getPassword();
        if (!SignupValidator.pwValid(lawPassword)){
            throw new IllegalArgumentException("비밀번호 형식이 잘못되었습니다.");
        }

        // password 암호화
        String encodedPassword = passwordEncoder.encode(lawPassword + SCERET_KEY);
        signupRequestDto.setPassword(encodedPassword);

        // 저장
        User user = new User(signupRequestDto);
        signupRepository.save(user);
        return user;
    }
}

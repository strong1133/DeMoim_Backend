package com.demoim_backend.demoim_backend.util;

import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.SignupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DuplicateChecker {

    private final SignupRepository signupRepository;

    // username
    public boolean duplicateChkUsername(String username) {
        Optional<User> findUsername = signupRepository.findByUsername(username);
        if (findUsername.isPresent()) {
            return false;
        }
        return true;
    }

    // nickname
    public boolean duplicateChkNickname(String nickname) {
        Optional<User> findNickname = signupRepository.findByNickname(nickname);
        if (findNickname.isPresent()) {
            return false;
        }
        return true;
    }
}

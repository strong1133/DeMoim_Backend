package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findCurUser(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        // 그 중에서도 primary Key인 id값
        Long user_id = principalDetails.getUser().getId();
        System.out.println(user_id);
        return userRepository.findById(user_id);
    }
}

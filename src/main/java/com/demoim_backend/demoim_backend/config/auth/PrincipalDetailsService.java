package com.demoim_backend.demoim_backend.config.auth;


import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        System.out.println("PrincipalDetailsService : 진입");
        User user = userRepository.findByUsername(username);
        System.out.println("요기서 에러가 나는건가?");
        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }


}


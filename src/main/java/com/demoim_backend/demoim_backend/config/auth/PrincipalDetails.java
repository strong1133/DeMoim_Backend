package com.demoim_backend.demoim_backend.config.auth;


import com.demoim_backend.demoim_backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        System.out.println("user :" + user);
        return user;
    }

    @Override
    public String getPassword ()  {
        System.out.println("pw :" + "진입");
        String pw = null;
        try {
            pw = user.getPassword();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("로그인이 정보가 잘못 되었습니다.");

        }
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        String id = null;
        try {
            id = user.getUsername();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("로그인 정보가 잘못 되었습니다.");

        }
        return user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        return authorities;
    }

}

package com.demoim_backend.demoim_backend.config;

import com.demoim_backend.demoim_backend.config.jwt.JwtAuthenticationFilter;
import com.demoim_backend.demoim_backend.config.jwt.JwtAuthorizationFilter;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import com.demoim_backend.demoim_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 시큐리티 적용 시작
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final ApplyInfoRepository applyInfoRepository;
    private final CorsConfig corsConfig;

    // 패스워드 인코더 DI
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager(), applyInfoRepository);
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(authenticationManager(), userRepository);
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http.headers().frameOptions().sameOrigin(); // H2콘솔 사용을 위한 추가
        http
                .cors().configurationSource(corsConfig.corsConfigurationSource())
                .and()
                .csrf().disable() // 사이트간 위조 요청 방지(보안)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함
                .and()
                .formLogin().disable() //시큐리티 defalut Login 사용 안함
                .httpBasic().disable()
                .addFilter(authenticationFilter)
                .addFilter(authorizationFilter)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();

    }

}

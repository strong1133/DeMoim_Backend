package com.demoim_backend.demoim_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration // DI를 위한 어노테이션
public class CorsConfig {

    @Bean // DI 추가
    public CorsConfigurationSource corsConfigurationSource(){

        // 클라이언트 요청이 쿠키를 통해서 자격 증명을 해야 하는 경우에 true.
        //true 일경우 클라이언트는 정의된 쿠키/토큰을 같이 넘겨 한다.
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("FETCH");

        config.setExposedHeaders(Arrays.asList("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}

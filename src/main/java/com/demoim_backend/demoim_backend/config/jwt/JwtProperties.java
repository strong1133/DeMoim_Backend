package com.demoim_backend.demoim_backend.config.jwt;

public interface JwtProperties {
    String SECRET = "DEMOIM_BACK_SECRET";
    int EXPIRATION_TIME = 12340000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}

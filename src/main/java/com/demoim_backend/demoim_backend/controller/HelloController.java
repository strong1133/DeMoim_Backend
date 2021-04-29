package com.demoim_backend.demoim_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String helloWorld(){
        return "반갑습니다!! DeMoim 한번 시작해 봅시다!😃😃";
    }
}

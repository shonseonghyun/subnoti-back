package com.sunghyun.football.domain.member.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminApi {

    @GetMapping("")
    public String test(){
        return "admin";
    }
}

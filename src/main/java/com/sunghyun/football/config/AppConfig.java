package com.sunghyun.football.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelCategory;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper(){
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("genderRule", GenderRule.class);
        enumMapper.put("levelRule", MemberLevelType.class);
        enumMapper.put("levelType", MemberLevelCategory.class);
        return enumMapper;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}

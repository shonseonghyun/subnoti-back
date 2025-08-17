package com.sunghyun.football.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.enumMapper.enums.EnumMapper;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumRuleMapper(){
        EnumMapper enumMapper = new EnumMapper();
//        enumMapper.put("genderRule", GenderRule.class);
//        enumMapper.put("levelRule", MemberLevelType.class);
//        enumMapper.put("levelType", MemberLevelCategory.class);
        enumMapper.put("subType", FreeSubType.class);
        enumMapper.put("gender", Gender.class);
        return enumMapper;
    }

//    @Bean
//    public EnumMapper enumSubTypesMapper(){
//        EnumMapper enumMapper = new EnumMapper();
//        enumMapper.put("freeSubType", FreeSubType.class);
//        return enumMapper;
//    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}

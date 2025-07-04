package com.sunghyun.football.domain.enumMapper.enums;

import lombok.Getter;

@Getter
public class EnumMapperValue {
    private String name;
    private String desc;

    public EnumMapperValue(EnumMapperType enumMapperType){
        this.name= enumMapperType.getName();
        this.desc =enumMapperType.getDesc();
    }
}

package com.sunghyun.football.domain.stadium.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class EnumMapper {
    private Map<String,List<EnumMapperValue>> factory = new LinkedHashMap<>();

    public void put(String key,Class<? extends EnumMapperType> e){
        factory.put(key,toEnumValues(e));
    }

    private List<EnumMapperValue> toEnumValues(Class<? extends EnumMapperType> e){
        return Arrays.stream(e.getEnumConstants())
                .map(EnumMapperValue::new)
                .collect(Collectors.toList());
    }

    public List<EnumMapperValue> get(String key){
        return factory.get(key);
    }

    public Map getAll(){
        return factory;
    }
}

package com.sunghyun.football.global.enums;

import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.domain.stadium.enums.EnumMapperValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class EnumMapperTest {

    @DisplayName("Enum 변환")
    @Test
    void convertEnumToValue(){
        final String key = "genderRule";
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put(key, GenderRule.class);
        List<EnumMapperValue> list = enumMapper.get(key);

        Assertions.assertThat(list.size()).isEqualTo(3);
    }

}
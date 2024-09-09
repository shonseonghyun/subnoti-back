package com.sunghyun.football.domain.stadium.application.dto;

import com.sunghyun.football.domain.stadium.domain.Location;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StadiumLocationDto {
    @NotNull
    private String province;

    @NotNull
    private String city;

    @NotNull
    private String address;

    public static StadiumLocationDto from(Location location) {
        StadiumLocationDto stadiumLocationDto =  new StadiumLocationDto();
        stadiumLocationDto.province = location.getProvince();
        stadiumLocationDto.city = location.getCity();
        stadiumLocationDto.address= location.getAddress();
        return stadiumLocationDto;
    }

    public Location toModel(){
        return Location.builder()
                .province(province)
                .city(city)
                .address(address)
                .build()
                ;
    }
}

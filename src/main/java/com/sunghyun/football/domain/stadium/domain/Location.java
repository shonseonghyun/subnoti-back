package com.sunghyun.football.domain.stadium.domain;

import lombok.*;

@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Long locationNo;

    private String province;

    private String city;

    private String address;

    public void setLocationNo(Long locationNo){
        this.locationNo = locationNo;
    }
}

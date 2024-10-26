package com.sunghyun.football.global.feign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlabMatchInfoResDto {
    private Long id;
    private String label_title;
    private Date schedule;
    private boolean is_manager_free;
    private boolean is_super_sub;
}

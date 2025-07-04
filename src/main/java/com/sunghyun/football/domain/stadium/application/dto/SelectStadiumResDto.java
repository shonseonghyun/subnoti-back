//package com.sunghyun.football.domain.stadium.application.dto;
//
//import com.sunghyun.football.domain.stadium.domain.Stadium;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class SelectStadiumResDto {
//    private Long stadiumNo;
//    private String stadiumName;
//    private StadiumLocationDto location;
//    private List<StadiumImageDto> imageList;
//
//    public static SelectStadiumResDto from(Stadium stadium) {
//        SelectStadiumResDto selectStadiumResDto = new SelectStadiumResDto();
//        selectStadiumResDto.stadiumNo = stadium.getStadiumNo();
//        selectStadiumResDto.stadiumName=stadium.getStadiumName();
//        selectStadiumResDto.location = StadiumLocationDto.from(stadium.getLocation());
//        selectStadiumResDto.imageList = stadium.getImageList().stream()
//                .map(stadiumImage -> StadiumImageDto.from(stadiumImage)).collect(Collectors.toList());
//        return selectStadiumResDto;
//    }
//}

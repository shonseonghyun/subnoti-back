//package com.sunghyun.football.domain.stadium.application.dto;
//
//import com.sunghyun.football.domain.stadium.domain.StadiumImage;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class StadiumImageDto {
//    private Long imageNo;
//
//    @NotNull
//    private String newFileName;
//
//    @NotNull
//    private String filePath;
//
//    public static StadiumImageDto from(StadiumImage stadiumImage) {
//        StadiumImageDto stadiumImageDto = new StadiumImageDto();
//        stadiumImageDto.imageNo = stadiumImage.getImageNo();
//        stadiumImageDto.newFileName= stadiumImage.getNewFileName();
//        stadiumImageDto.filePath = stadiumImage.getFilePath();
//        return stadiumImageDto;
//    }
//
//    public StadiumImage toModel(){
//        return StadiumImage.builder()
//                .imageNo(imageNo)
//                .newFileName(newFileName)
//                .filePath(filePath)
//                .build()
//                ;
//    }
//}

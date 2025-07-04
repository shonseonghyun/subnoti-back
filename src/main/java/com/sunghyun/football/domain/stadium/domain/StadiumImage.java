//package com.sunghyun.football.domain.stadium.domain;
//
//import lombok.*;
//
//import java.util.Objects;
//
//@ToString
//@Builder
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class StadiumImage {
//    private Long imageNo;
//
//    private String filePath;
//
//    private String originalFileName;
//
//    private String newFileName;
//
//    private Integer fileSize;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        StadiumImage that = (StadiumImage) o;
//        //imageNo 값만 비교
//        return Objects.equals(getImageNo(), that.getImageNo());
//    }
//}

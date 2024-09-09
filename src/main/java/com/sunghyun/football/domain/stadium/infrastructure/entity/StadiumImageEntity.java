package com.sunghyun.football.domain.stadium.infrastructure.entity;

import com.sunghyun.football.domain.stadium.domain.StadiumImage;
import jakarta.persistence.*;

@Table(name = "stadium_images")
@Entity
public class StadiumImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageNo;

    private String filePath;

    private String originalFileName;

    private String newFileName;

    private Integer fileSize;

    public static StadiumImageEntity from(StadiumImage stadiumImage){
        StadiumImageEntity stadiumImageEntity = new StadiumImageEntity();
        stadiumImageEntity.imageNo=stadiumImage.getImageNo();
        stadiumImageEntity.originalFileName= stadiumImage.getOriginalFileName();
        stadiumImageEntity.newFileName = stadiumImage.getNewFileName();
        stadiumImageEntity.fileSize = stadiumImage.getFileSize();
        stadiumImageEntity.filePath = stadiumImage.getFilePath();
        return stadiumImageEntity;
    }

    public StadiumImage toModel(){
        return StadiumImage.builder()
                .imageNo(imageNo)
                .originalFileName(originalFileName)
                .newFileName(newFileName)
                .fileSize(fileSize)
                .filePath(filePath)
                .build()
                ;
    }
}

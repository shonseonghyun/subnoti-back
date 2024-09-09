package com.sunghyun.football.domain.stadium.domain;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumImageExceedException;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Stadium {
    private Long stadiumNo;

    private String stadiumName;

    private Location location;

    private List<StadiumImage> imageList;

    public static Stadium createStadium(String stadiumName,Location location,List<StadiumImage> imageList){
        Stadium stadium = new Stadium();
        stadium.stadiumName = stadiumName;
        stadium.location = location;
        stadium.imageList = imageList;

        checkImageSize(stadium);

        return stadium;
    }

    private static void checkImageSize(Stadium stadium){
        if(stadium.imageList.size()>3){
            throw new StadiumImageExceedException(ErrorCode.STADIUM_IMAGE_EXCEED);
        }
    }

    public void updateStadium(String stadiumName,Location location,List<StadiumImage> imageList){
        changeStadiumName(stadiumName);
        changeLocation(location);
        changeImage(imageList);
    }

    private void changeStadiumName(String newStadiumName){
        setStadiumName(newStadiumName);
    }

    private void changeLocation(Location location) {
        location.setLocationNo(this.location.getLocationNo());
        setLocation(location);
    }

    private void changeImage(List<StadiumImage> updateImageList) {
        Map<Integer,StadiumImage> targetMap = new HashMap<>();

        if(updateImageList.isEmpty()){
            return;
        }

        for(StadiumImage target:updateImageList){
            Long imageNo = target.getImageNo();

            StadiumImage prev = imageList.stream()
                            .filter(stadiumImage -> stadiumImage.getImageNo().equals(imageNo)).findAny()
                            .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND.STADIUM_IMAGE_NOT_FOUND));

            int targetIdx =imageList.indexOf(target);
            targetMap.put(targetIdx,target);
        }

        targetMap.forEach((targetIdx,target)->imageList.set(targetIdx,target));
    }
}

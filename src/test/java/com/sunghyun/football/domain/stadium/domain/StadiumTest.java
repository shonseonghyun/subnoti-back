package com.sunghyun.football.domain.stadium.domain;

import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class StadiumTest {

    final String updateStadiumName= "충청 BB 스타디움";

    final String updateProvince= "충청도";
    final String updateCity="구장시";
    final String updateAddress = "568-22";

    final String newFileName1 = "새로변할파일명1";
    final String newFilePath1 = "새로변할파일경로1";
    final String newFileName2 = "새로변할파일명2";
    final String newFilePath2 = "새로변할파일경로2";

    @DisplayName("StadiumImage 도메인객체 equal 메소드 재정의 테스트")
    @Test
    void equalsTestInStadiumImage(){
        List<StadiumImage> list = new ArrayList<>();
        StadiumImage stadiumImage1= createStadiumImage(1L,"1번","1번 경로");
        StadiumImage stadiumImage2= createStadiumImage(2L,"2번","2번 경로");

        StadiumImage stadiumImage5= createStadiumImage(1L,"4번","4번 경로");
        StadiumImage stadiumImage6= createStadiumImage(4L,"5번","5번 경로");

        list.add(stadiumImage1);
        list.add(stadiumImage2);

        Assertions.assertThat(list.contains(stadiumImage1)).isEqualTo(true);
        Assertions.assertThat(list.contains(stadiumImage5)).isEqualTo(true);
        Assertions.assertThat(list.contains(stadiumImage6)).isEqualTo(false);
        Assertions.assertThat(list.indexOf(stadiumImage5)).isEqualTo(0);
    }



    @DisplayName("특정 스타디움이미지 변경 시 존재하지 않는 이미지 번호 인입 시 익셉션 처리")
    @Test
    void changeImageByNotExistImageNo(){
        //given
        StadiumImage stadiumImageForChange= createStadiumImage(3L,"새로 변할 파일명","새로 재정의될 파일 경로");

        List<StadiumImage> list = new ArrayList<>();
        list.add(createStadiumImage(1L,"1번","1번 경로"));
        list.add(createStadiumImage(2L,"2번","2번 경로"));

        Stadium stadium = Stadium.builder()
                .imageList(list)
                .location(createLocation())
                .build();

        //then
        Assertions.assertThatThrownBy(()->stadium.updateStadium(null,createLocation(),Arrays.asList(stadiumImageForChange)))
                .isInstanceOf(StadiumNotFoundException.class);
    }

    @DisplayName("스타디움 업데이트")
    @Test
    void updateStadium(){
        //given
        final Long imageNo1 =1L;
        final Long imageNo2 =2L;

        final int idx1;
        final int idx2;
        List<StadiumImage> list = new ArrayList<>();

        Location locationForChange = createLocation();
        StadiumImage stadiumImageForChange1= createStadiumImage(1L,newFileName1,newFilePath1);
        StadiumImage stadiumImageForChange2= createStadiumImage(2L,newFileName2,newFilePath2);
        list.add(createStadiumImage(imageNo1,"1번","1번 경로"));
        list.add(createStadiumImage(imageNo2,"2번","2번 경로"));
        Stadium stadium = Stadium.builder()
                .location(createLocation())
                .imageList(list)
                .build();

        //when
        stadium.updateStadium(updateStadiumName,locationForChange,Arrays.asList(stadiumImageForChange1,stadiumImageForChange2));
        idx1 = stadium.getImageList().indexOf(stadiumImageForChange1);
        idx2 = stadium.getImageList().indexOf(stadiumImageForChange2);

        //then
        /*stadiumInfo*/
        Assertions.assertThat(stadium.getStadiumName()).isEqualTo(updateStadiumName);

        /*location*/
        Assertions.assertThat(stadium.getLocation().getProvince()).isEqualTo(updateProvince);
        Assertions.assertThat(stadium.getLocation().getCity()).isEqualTo(updateCity);
        Assertions.assertThat(stadium.getLocation().getAddress()).isEqualTo(updateAddress);

        /*imageList*/
        Assertions.assertThat(stadium.getImageList().size()).isEqualTo(2);
        Assertions.assertThat(stadium.getImageList().get(idx1).getImageNo()).isEqualTo(imageNo1);
        Assertions.assertThat(stadium.getImageList().get(idx2).getImageNo()).isEqualTo(imageNo2);
//        Assertions.assertThat(stadium.getImageList().get(idx1).getFileName()).isEqualTo(newFileName1);
        Assertions.assertThat(stadium.getImageList().get(idx2).getFilePath()).isEqualTo(newFilePath2);
    }

    private Location createLocation(){
        return Location.builder()
                .province(updateProvince)
                .city(updateCity)
                .address(updateAddress)
                .build()
                ;
    }

    private StadiumImage createStadiumImage(Long imageNo, String fileName, String filePath){
        return  StadiumImage.builder()
                .imageNo(imageNo)
//                .fileName(fileName)
                .filePath(filePath)
                .build()
                ;
    }
}
package com.sunghyun.football.domain.stadium.domain;

import com.sunghyun.football.domain.stadium.domain.repository.StadiumRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
import com.sunghyun.football.repository.TestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

//@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class))
//@ActiveProfiles("dev")
////@ActiveProfiles("local")
//@Rollback(value = false)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StadiumRepositoryTest extends TestRepository {

    @Autowired
    private StadiumRepository stadiumRepository;

    private final String province= "경기도";
    private final String city="수원시";
    private final String address = "255-1";
    private final String updateProvince= "충청도";
    private final String updateCity="구장시";
    private final String updateAddress = "568-22";

    private final String stadiumName= "수원 HK 스타디움";
    private final String updateStadiumName= "충청 BB 스타디움";

    private final String fileName = "테스트파일";
    private final String filePath= "/c/c";
    private final String updateFileName = "업데이트파일";
    private final String updateFilePath= "/upd/upd";

    @DisplayName("스타디움 저장")
    @Test
    void createStadium(){
        //given
        final Stadium stadium = Stadium.builder()
                .stadiumName(stadiumName)
                .location(createNewLocation())
                .imageList(Arrays.asList(createNewStadiumImage(),createNewStadiumImage()))
                .build();

        //when
        Stadium savedStadium = stadiumRepository.save(stadium);

        //then
        Assertions.assertThat(savedStadium.getLocation().getProvince()).isEqualTo(province);
        Assertions.assertThat(savedStadium.getLocation().getCity()).isEqualTo(city);
        Assertions.assertThat(savedStadium.getLocation().getAddress()).isEqualTo(address);
    }

//    @DisplayName("스타디움 업데이트")
//    @Test
//    void updateLocation(){
//        //given
//        final Stadium stadium = createNewStadium();
//        Stadium savedStadium = stadiumRepository.save(stadium);
//
//        final Long stadiumNo = savedStadium.getStadiumNo();
//        final Long locationNo = savedStadium.getLocation().getLocationNo();
//        final Long imageNo = savedStadium.getImageList().get(0).getImageNo();
//
//        final StadiumImage updateImage = createUpdateImage(imageNo);
//        final int index ;
//
//        //when
//        savedStadium.updateStadium(updateStadiumName,createUpdateLocation(),Arrays.asList(updateImage));
//        Stadium updatedStadium = stadiumRepository.save(savedStadium);
//        index = updatedStadium.getImageList().indexOf(updateImage);
//
//        //then
//        Assertions.assertThat(updatedStadium.getStadiumNo()).isEqualTo(stadiumNo);
//        Assertions.assertThat(updatedStadium.getStadiumName()).isEqualTo(updateStadiumName);
//
//        Assertions.assertThat(updatedStadium.getLocation().getLocationNo()).isEqualTo(locationNo);
//        Assertions.assertThat(updatedStadium.getLocation().getAddress()).isEqualTo(updateAddress);
//
//        Assertions.assertThat(updatedStadium.getImageList().get(updatedStadium.getImageList().indexOf(updateImage)).getImageNo()).isEqualTo(imageNo);
//        Assertions.assertThat(updatedStadium.getImageList().size()).isEqualTo(2);
//        Assertions.assertThat(updatedStadium.getImageList().get(index).getFilePath()).isEqualTo(updateFilePath);
////        Assertions.assertThat(updatedStadium.getImageList().get(updatedStadium.getImageList().indexOf(updateImage)).getFileName()).isEqualTo(updateFileName);
//    }
    
    @DisplayName("스타디움 조회")
    @Test
    void getStadium(){
        //given
        final  Stadium stadium = createNewStadium();
        Stadium savedStadium = stadiumRepository.save(stadium);
        final Long stadiumNo = savedStadium.getStadiumNo();
        final Long imageNo = savedStadium.getImageList().get(0).getImageNo();
        final Long locationNo = savedStadium.getLocation().getLocationNo();

        //when
        Stadium selectedStadium = stadiumRepository.findByStadiumNo(stadiumNo)
                .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND));

        //then
        Assertions.assertThat(selectedStadium.getStadiumNo()).isEqualTo(stadiumNo);
        Assertions.assertThat(selectedStadium.getLocation().getLocationNo()).isEqualTo(locationNo);
        Assertions.assertThat(selectedStadium.getImageList().get(0).getImageNo()).isEqualTo(imageNo);
        Assertions.assertThat(selectedStadium.getLocation().getCity()).isEqualTo(city);
        Assertions.assertThat(selectedStadium.getImageList().get(0).getFilePath()).isEqualTo(filePath);
    }

    @DisplayName("스타디움 삭제")
    @Test
    void deleteStadium(){
        //given
        final  Stadium stadium = createNewStadium();
        Stadium savedStadium = stadiumRepository.save(stadium);
        final Long stadiumNo = savedStadium.getStadiumNo();

        //when
        stadiumRepository.delete(stadiumNo);

        //given
        Assertions.assertThatThrownBy(()->getStadiumById(stadiumNo))
                .isInstanceOf(StadiumNotFoundException.class);
    }

    private Stadium getStadiumById(Long stadiumNo){
        return stadiumRepository.findByStadiumNo(stadiumNo)
                .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND));
    }

    private Stadium createNewStadium(){
        return Stadium.builder()
                .stadiumName(stadiumName)
                .location(createNewLocation())
                .imageList(Arrays.asList(createNewStadiumImage(),createNewStadiumImage()))
                .build();
    }

    private Location createNewLocation(){
        return Location.builder()
                .province(province)
                .city(city)
                .address(address)
                .build();
    }

    private Location createUpdateLocation(){
        return Location.builder()
                .province(updateProvince)
                .city(updateCity)
                .address(updateAddress)
                .build();
    }

    private StadiumImage createUpdateImage(Long imageNo){
        return  StadiumImage.builder()
                .imageNo(imageNo)
//                .fileName(updateFileName)
                .filePath(updateFilePath)
                .build()
                ;
    }


    private StadiumImage createNewStadiumImage(){
        return  StadiumImage.builder()
//                .fileName(fileName)
                .filePath(filePath)
                .build()
                ;
    }
}
package com.sunghyun.football.domain.stadium.application;

import com.sunghyun.football.domain.stadium.application.dto.*;
import com.sunghyun.football.domain.stadium.domain.Location;
import com.sunghyun.football.domain.stadium.domain.Stadium;
import com.sunghyun.football.domain.stadium.domain.StadiumImage;
import com.sunghyun.football.domain.stadium.domain.repository.StadiumRepository;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumImageExceedException;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
import com.sunghyun.football.global.file.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StadiumApplicationTest {

    @InjectMocks
    StadiumApplication target;

    @Mock
    StadiumRepository stadiumRepository;

    @Mock
    FileService fileService;

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
    private final String updateFileName = "테스트파일";
    private final String updateFilePath= "/d/d";

    private final String newFileName = "newFileName";
    private final Integer fileSize = 1234;

    @DisplayName("스타디움 조회 실패_존재하지 않는 스타디움 번호 인입")
    @Test
    void getStadiumWithNotExistStadiumNo(){
        //given
        final Long stadiumNo =10L;
        doReturn(Optional.empty()).when(stadiumRepository).findByStadiumNo(stadiumNo);

        //when,then
        Assertions.assertThatThrownBy(()->target.getStadium(stadiumNo)).isInstanceOf(StadiumNotFoundException.class);
    }

    @DisplayName("스타디움 조회 성공")
    @Test
    void getStadium(){
        //given
        final Long stadiumNo =10L;
        final Stadium stadium = createStadium();
        doReturn(Optional.of(stadium)).when(stadiumRepository).findByStadiumNo(stadiumNo);

        //when
        SelectStadiumResDto selectStadiumResDto = target.getStadium(stadiumNo);

        //then
        Assertions.assertThat(selectStadiumResDto.getStadiumName()).isEqualTo(stadiumName);
        Assertions.assertThat(selectStadiumResDto.getImageList().size()).isEqualTo(2);
    }

    @DisplayName("스타디움 등록 시 등록이미지수 3장 초과인 경우 익셉션")
    @Test
    void regStadiumWithThreeExceedImages(){
        //given
        MockMultipartFile file = new MockMultipartFile("imgName1", "imgName1", MediaType.IMAGE_JPEG_VALUE, "img".getBytes());
        List<MultipartFile> multipartFiles = Arrays.asList(file,file,file,file);
        List<StadiumImage> imageList= Arrays.asList(createStadiumImage(),createStadiumImage(),createStadiumImage(),createStadiumImage());
        RegStadiumReqDto regStadiumReqDto = createRegStadiumReqDto();
        doReturn(imageList).when(fileService).doProcess(multipartFiles);

        //when,then
        Assertions.assertThatThrownBy(()->target.regStadium(regStadiumReqDto,multipartFiles))
                .isInstanceOf(StadiumImageExceedException.class);
    }

    @DisplayName("스타디움 등록 정상 처리")
    @Test
    void regStadium(){
        //given
//        List<StadiumImageDto> threeImages = Arrays.asList(createStadiumImageDto(1L),createStadiumImageDto(),createStadiumImageDto());
        RegStadiumReqDto regStadiumReqDto =createRegStadiumReqDto();
        List<MultipartFile> multipartFiles = Collections.emptyList();

        //when
        target.regStadium(regStadiumReqDto,multipartFiles);

        //then
        verify(stadiumRepository,times(1)).save(any(Stadium.class));
    }

//    @DisplayName("스타디움 업데이트 시 존재하지 않는 스타디움 번호 인입 시 익셉션 처리")
//    @Test
//    void updateStadiumWithNotExistStadiumNo(){
//        //given
//        final Long stadiumNo = 1L;
//        final Long imageNo = 1L;
//        List<StadiumImageDto> imageList =Arrays.asList(createStadiumImageDto(imageNo));
//
//        UpdateStadiumReqDto updateStadiumReqDto = createUpdateStadiumReqDto(null,imageList);
//
//        doReturn(Optional.empty()).when(stadiumRepository).findByStadiumNo(stadiumNo);
//
//        //when,then
//        Assertions.assertThatThrownBy(()->target.updateStadium(stadiumNo,updateStadiumReqDto))
//                .isInstanceOf(StadiumNotFoundException.class);
//    }
//
//    @DisplayName("스타디움 업데이트 정상처리_파라미터가 null(location,stadiumImages)")
//    @ParameterizedTest
//    @MethodSource("invalidStadiumUpdateParameter")
//    void updateStadiumWithNotExistImageList(final StadiumLocationDto stadiumLocationDto,List<StadiumImageDto> imageList){
//        //given
//        final Long stadiumNo = 1L;
//        final Long locationNo = 2L;
//        UpdateStadiumReqDto updateStadiumReqDto = createUpdateStadiumReqDto(stadiumLocationDto,imageList);
//        Stadium stadium = createStadium();
//
//        doReturn(Optional.of(stadium)
//        ).when(stadiumRepository).findByStadiumNo(stadiumNo);
//
//        //when
//        target.updateStadium(stadiumNo,updateStadiumReqDto);
//
//        //then
//        verify(stadiumRepository,times(1)).save(any(Stadium.class));
//        if(imageList!=null){
//            Assertions.assertThat(stadium.getImageList().size()).isEqualTo(2);
//        }
//    }
//
//    private static Stream<Arguments> invalidStadiumUpdateParameter(){
//        return Stream.of(
//                Arguments.of(StadiumLocationDto.builder().build(),null),
//                Arguments.of(null,new ArrayList<StadiumImageDto>())
////                Arguments.of(null,Arrays.asList(StadiumImageDto.builder().imageNo(1L).build()))
//        );
//    }
//
//    @DisplayName("스타디움 업데이트 시 존재하지 않는 이미지번호로 요청 시 익셉션 처리")
//    @Test
//    void updateStadiumWithNotExistImageNo(){
//        //given
//        final Long stadiumNo = 1L;
//        final Long locationNo = 1L;
//        final Long imageNo1 = 1L;
//        final Long imageNo2 = 2L;
//        List<StadiumImageDto> imageList =Arrays.asList(createStadiumImageDto(imageNo1));
//
//        UpdateStadiumReqDto updateStadiumReqDto = createUpdateStadiumReqDto(null,imageList);
//
//        Stadium selectedStadium = Stadium.builder()
//                    .stadiumNo(stadiumNo)
//                    .stadiumName(stadiumName)
//                    .location(createLocation(locationNo))
//                    .imageList(Arrays.asList(createStadiumImage(imageNo2)))
//                            .build();
//
//        doReturn(Optional.of(selectedStadium)).when(stadiumRepository).findByStadiumNo(stadiumNo);
//
//        //then
//        Assertions.assertThatThrownBy(()->target.updateStadium(stadiumNo,updateStadiumReqDto))
//                .isInstanceOf(StadiumNotFoundException.class)
//                ;
//    }
//
//    @DisplayName("스타디움 업데이트 정상 처리")
//    @Test
//    void updateStadium(){
//        final Long stadiumNo = 1L;
//        final Long imageNo = 1L;
//
//        List<StadiumImageDto> imageList =Arrays.asList(createStadiumImageDto(imageNo));
//        UpdateStadiumReqDto updateStadiumReqDto = createUpdateStadiumReqDto(createStadiumLocationDto(updateProvince,updateCity,updateAddress),imageList);
//
//        doReturn(Optional.of(Stadium.createStadium(stadiumName,createLocation(),Arrays.asList(createStadiumImage(1L),createStadiumImage(2L))))).when(stadiumRepository).findByStadiumNo(stadiumNo);
//
//        //when
//        target.updateStadium(stadiumNo,updateStadiumReqDto);
//
//        //then
//        verify(stadiumRepository).save(any(Stadium.class));
//    }
    
    @DisplayName("스타디움 삭제_존재하지 않는 스타디움번호 인입으로 인한 익셉션 처리")
    @Test
    void deleteStadiumByNotExistStadiumNo(){
        //given
        final Long unknownstadiumNo = 10L;
        doReturn(Optional.empty()).when(stadiumRepository).findByStadiumNo(unknownstadiumNo);

        //when,then
        Assertions.assertThatThrownBy(()->target.deleteStadium(unknownstadiumNo))
                .isInstanceOf(StadiumNotFoundException.class);
    }

    @DisplayName("스타디움 삭제 정상 처리")
    @Test
    void deleteStadiumByStadiumNo(){
        //given
        final Long stadiumNo = 1L;
        Stadium stadium = createStadium();
        doReturn(Optional.of(stadium)).when(stadiumRepository).findByStadiumNo(stadiumNo);

        //when
        target.deleteStadium(stadiumNo);

        //then
        verify(stadiumRepository,times(1)).delete(stadiumNo);
    }

    private RegStadiumReqDto createRegStadiumReqDto(){
        return RegStadiumReqDto.builder()
                .stadiumName(stadiumName)
                .stadiumLocation(createStadiumLocationDto(province,city,address))
                .build()
                ;
    }

    private UpdateStadiumReqDto createUpdateStadiumReqDto(StadiumLocationDto location,List<StadiumImageDto> imageList){
        return UpdateStadiumReqDto.builder()
                .stadiumName(updateStadiumName)
                .stadiumLocationDto(location)
//                .imageDtoList(imageList)
                .build()
                ;
    }

    private StadiumLocationDto createStadiumLocationDto(String province,String city,String address){
        return StadiumLocationDto.builder()
                .province(province)
                .city(city)
                .address(address)
                .build()
                ;
    }



    private StadiumImageDto createStadiumImageDto(){
        return StadiumImageDto.builder()
//                .fileName(fileName)
                .filePath(filePath)
                .build()
                ;
    }

    private StadiumImageDto createStadiumImageDto(Long imageNo){
        return StadiumImageDto.builder()
                .imageNo(imageNo)
//                .fileName(fileName)
                .filePath(filePath)
                .build()
                ;
    }

    private Location createLocation(){
        return Location.builder()
                .province(province)
                .city(city)
                .address(address)
                .build();
    }

    private Location createLocation(Long locationNo){
        return Location.builder()
                .locationNo(locationNo)
                .province(province)
                .city(city)
                .address(address)
                .build();
    }

    private StadiumImage createStadiumImage(){
        return  StadiumImage.builder()
//                .fileName(fileName)
                .filePath(filePath)
                .build()
                ;
    }

    private StadiumImage createStadiumImage(Long imageNo){
        return  StadiumImage.builder()
                .imageNo(imageNo)
                .newFileName(newFileName)
                .fileSize(fileSize)
                .filePath(filePath)
                .build()
                ;
    }

    private Stadium createStadium(){
        return Stadium.builder()
                .stadiumName(stadiumName)
                .location(createLocation(1L))
                .imageList(Arrays.asList(createStadiumImage(1L),createStadiumImage(2L)))
                .build();
    }


}
package com.sunghyun.football.domain.stadium.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.stadium.application.StadiumApplication;
import com.sunghyun.football.domain.stadium.application.dto.RegStadiumReqDto;
import com.sunghyun.football.domain.stadium.application.dto.StadiumImageDto;
import com.sunghyun.football.domain.stadium.application.dto.StadiumLocationDto;
import com.sunghyun.football.domain.stadium.application.dto.UpdateStadiumReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StadiumApi.class)
class StadiumApiTest {

    @MockBean
    private StadiumApplication stadiumApplication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String province= "경기도";
    private final static String city="수원시";
    private final static String address = "255-1";

    private final static String updateProvince= "충청도";
    private final static String updateCity="구장시";
    private final static String updateAddress = "568-22";

    private final static String stadiumName= "수원 HK 스타디움";

    private final static String updateStadiumName= "충청 BB 스타디움";

    private final static String fileName = "테스트파일";
    private final static String filePath= "/c/c";

    private final static String updateFileName = "테스트파일";
    private final static String updateFilePath= "/d/d";

    @DisplayName("스타디움 등록 실패_등록파라미터 NotNull 체크")
    @ParameterizedTest
    @MethodSource("invalidRegStadiumParameter")
    void regStadiumWithParameterIsNull(final String stadiumName,final StadiumLocationDto stadiumLocationDto) throws Exception {
        //given
        final String url = "/api/v1/stadium";
        final RegStadiumReqDto regStadiumReqDto = RegStadiumReqDto.builder()
                .stadiumName(stadiumName)
                .stadiumLocation(stadiumLocationDto)
                .build()
                ;
        final String content = objectMapper.writeValueAsString(regStadiumReqDto);

        //when
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .file(new MockMultipartFile("regStadiumReqDto","", "application/json",content.getBytes(StandardCharsets.UTF_8)))
                .contentType("multipart/form-data"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()));
    }

    private static Stream<Arguments> invalidRegStadiumParameter(){
        return Stream.of(
                Arguments.of(null,createStadiumLocationDto()),
                Arguments.of(stadiumName,null)
        );
    }

    @DisplayName("스타디움 등록 실패_등록 파라미터의 중첩된 DTO NotNull 체크")
//    @ParameterizedTest
//    @MethodSource("invalidRegStadiumParameterInParameter")
    @Test
    void regStadiumWithParameterInParameterIsNull() throws Exception {
        //given
        final String url = "/api/v1/stadium";
        final RegStadiumReqDto regStadiumReqDto = RegStadiumReqDto.builder()
                .stadiumName(stadiumName)
                .stadiumLocation(createStadiumLocationDtoWithProvinceIsNull())
                .build()
                ;
        final String content = objectMapper.writeValueAsString(regStadiumReqDto);
        //when
        final ResultActions resultActions = mockMvc.perform(
                        multipart(url)
                        .file(new MockMultipartFile("regStadiumReqDto","", "application/json",content.getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()));
        ;
    }

    @DisplayName("스타디움 등록 실패_이미지가 등록되지 않거나 3개 초과")
    @Test
    void regStadiumWithImageSizeInvalid() throws Exception {
        //given
        final String url = "/api/v1/stadium";
        final RegStadiumReqDto regStadiumReqDto = RegStadiumReqDto.builder()
                .stadiumName(stadiumName)
                .stadiumLocation(createStadiumLocationDto())
                .build()
                ;

        final String content = objectMapper.writeValueAsString(regStadiumReqDto);


        //when
        final ResultActions resultActions = mockMvc.perform(
                        multipart(url)
                        .file(new MockMultipartFile("regStadiumReqDto","", "application/json",content.getBytes(StandardCharsets.UTF_8)))
//                        .content(objectMapper.writeValueAsString(regStadiumReqDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.STADIUM_IMAGE_EXCEED.getCode()));
    }

    @DisplayName("스타디움 등록 성공")
    @Test
    void regStadium() throws Exception{
        //given
        final String url = "/api/v1/stadium";
        final RegStadiumReqDto regStadiumReqDto = RegStadiumReqDto.builder()
                .stadiumName(stadiumName)
                .stadiumLocation(createStadiumLocationDto())
                .build()
                ;
        final String content = objectMapper.writeValueAsString(regStadiumReqDto);
        //when
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .file(new MockMultipartFile("regStadiumReqDto","", "application/json",content.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()));
    }

    @DisplayName("스타디움 조회 실패_스타디움번호가 아닌 문자열 인입")
    @Test
    void getStadiumWithStadiumNoIsString() throws Exception {
        final String stadiumNo="salsa";
        final String url = "/api/v1/stadium/"+stadiumNo;

        final ResultActions resultActions = mockMvc.perform(get(url));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()));
    }
    @DisplayName("스타디움 조회 성공")
    @Test
    void getStadiumWithStadium() throws Exception {
        final Long stadiumNo=1L;
        final String url = "/api/v1/stadium/"+stadiumNo;

        final ResultActions resultActions = mockMvc.perform(get(url));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()));
    }



//    @DisplayName("스타디움 업데이트 실패_스타디움번호가 아닌 문자연 인입")
//    @Test
//    void updateStadiumWithStadiumNoIsString() throws Exception{
//        //given
//        final String url = "/api/v1/stadium/asd";
//        final UpdateStadiumReqDto updateStadiumReqDto = UpdateStadiumReqDto.builder()
//                .stadiumName(stadiumName)
//                .stadiumLocationDto(createStadiumLocationDto())
////                .imageDtoList(Arrays.asList(createStadiumImageDto()))
//                .build()
//                ;
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(put(url)
//                .content(objectMapper.writeValueAsString(updateStadiumReqDto))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("스타디움 업데이트 성공")
//    @Test
//    void updateStadium() throws Exception{
//        //given
//        final Long stadiumNo = 1L;
//        final String url = "/api/v1/stadium/"+stadiumNo;
//        final UpdateStadiumReqDto updateStadiumReqDto = UpdateStadiumReqDto.builder()
//                .stadiumName(stadiumName)
//                .stadiumLocationDto(createStadiumLocationDto())
////                .imageDtoList(Arrays.asList(createStadiumImageDto()))
//                .build()
//                ;
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(put(url)
//                .content(objectMapper.writeValueAsString(updateStadiumReqDto))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions.andExpect(status().isOk());
//    }

    @DisplayName("스타디움 삭제 실패_존재하지 않는 스타디움번호")
    @Test
    void deleteStadiumWithNotExistStadiumNo() throws Exception {
        //given
        final Long stadiumNo = 1L;
        final String url = "/api/v1/stadium/"+stadiumNo;
        doThrow(new StadiumNotFoundException((ErrorCode.STADIUM_NOT_FOUND)))
                .when(stadiumApplication).deleteStadium(stadiumNo);

        //when
        final ResultActions resultActions = mockMvc.perform(delete(url));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.STADIUM_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorCode.STADIUM_NOT_FOUND.getMessage()));
        ;
    }

    @DisplayName("스타디움 삭제 성공")
    @Test
    void deleteStadium() throws Exception {
        //given
        final Long stadiumNo = 1L;
        final String url = "/api/v1/stadium/"+stadiumNo;

        //when
        final ResultActions resultActions = mockMvc.perform(delete(url));

        //then
        resultActions.andExpect(status().isOk());
    }


    private static StadiumLocationDto createStadiumLocationDto(){
        return StadiumLocationDto.builder()
                .province(province)
                .city(city)
                .address(address)
                .build()
                ;
    }

    private static StadiumLocationDto createStadiumLocationDtoWithProvinceIsNull(){
        return StadiumLocationDto.builder()
                .city(city)
                .address(address)
                .build()
                ;
    }


//    private static StadiumImageDto createStadiumImageDto(){
//        return StadiumImageDto.builder()
//                .new(fileName)
//                .filePath(filePath)
//                .build()
//                ;
//    }
//
//    private static StadiumImageDto createStadiumImageDtoWithFileNameIsNull(){
//        return StadiumImageDto.builder()
//                .filePath(filePath)
//                .build()
//                ;
//    }


}
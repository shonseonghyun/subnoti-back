//package com.sunghyun.football.domain.stadium.application;
//
//import com.sunghyun.football.domain.stadium.application.dto.*;
//import com.sunghyun.football.domain.stadium.domain.Location;
//import com.sunghyun.football.domain.stadium.domain.Stadium;
//import com.sunghyun.football.domain.stadium.domain.StadiumImage;
//import com.sunghyun.football.domain.stadium.domain.repository.StadiumRepository;
//import com.sunghyun.football.global.exception.ErrorCode;
//import com.sunghyun.football.global.exception.exceptions.stadium.StadiumNotFoundException;
//import com.sunghyun.football.global.file.FileService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class StadiumApplication {
//
//    private final StadiumRepository stadiumRepository;
//
//    private final FileService fileService;
//
//    public void regStadium(final RegStadiumReqDto regStadiumReqDto, final List<MultipartFile> stadiumImageFiles){
//        Location location = regStadiumReqDto.getStadiumLocation().toModel();
//        List<StadiumImage> images = fileService.doProcess(stadiumImageFiles);
////        fileService.uploadFile();
//        Stadium stadium = Stadium.createStadium(regStadiumReqDto.getStadiumName(),location,images);
//
//        stadiumRepository.save(stadium);
//    }
//
//    public void deleteStadium(final Long stadiumNo){
//        Stadium selectedStadium = stadiumRepository.findByStadiumNo(stadiumNo)
//                .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND));
//
//        stadiumRepository.delete(stadiumNo);
//    }
//
//    public void updateStadium(final Long stadiumNo,final UpdateStadiumReqDto updateStadiumReqDto) {
//        Stadium selectedStadium = stadiumRepository.findByStadiumNo(stadiumNo)
//                .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND));
//
////        Location location = updateStadiumReqDto.getLocation().toModel();
////        List<StadiumImage> imageList  = updateStadiumReqDto.getImageList().stream().map(StadiumImageDto::toModel).collect(Collectors.toList());
//        Location location = getLocationFromDto(updateStadiumReqDto.getStadiumLocationDto());
////        List<StadiumImage> imageList = getStadiumImageListFromDto(updateStadiumReqDto.getImageDtoList());
//        List<StadiumImage> imageList = null;
//        selectedStadium.updateStadium(updateStadiumReqDto.getStadiumName(),location,imageList);
//
//        stadiumRepository.save(selectedStadium);
//    }
//
//    private List<StadiumImage> getStadiumImageListFromDto(final List<StadiumImageDto> dtoImageList){
//        List<StadiumImageDto> targetList = (dtoImageList == null)
//                ? Collections.emptyList()
//                : dtoImageList
//                ;
//
//        List<StadiumImage> imageList = targetList.stream()
//                .map(StadiumImageDto::toModel)
//                .collect(Collectors.toList())
//                ;
//        return imageList;
//    }
//
//    private Location getLocationFromDto(final StadiumLocationDto stadiumLocationDto){
//        Location location = Optional.ofNullable(stadiumLocationDto)
//                .map(StadiumLocationDto::toModel)
//                .orElseGet(Location::new);
//        return location;
//    }
//
//    public SelectStadiumResDto getStadium(final Long stadiumNo) {
//        Stadium stadium = stadiumRepository.findByStadiumNo(stadiumNo)
//                .orElseThrow(()->new StadiumNotFoundException(ErrorCode.STADIUM_NOT_FOUND));
//        SelectStadiumResDto selectStadiumResDto= SelectStadiumResDto.from(stadium);
//        return selectStadiumResDto;
//    }
//}

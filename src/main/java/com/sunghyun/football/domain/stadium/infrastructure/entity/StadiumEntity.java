//package com.sunghyun.football.domain.stadium.infrastructure.entity;
//
//
//import com.sunghyun.football.domain.stadium.domain.Stadium;
//import jakarta.persistence.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Table(name = "stadium")
//@Entity
//public class StadiumEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long stadiumNo;
//
//    @Column
//    private String stadiumName;
//
//    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true,fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_no") //외래키를 들고 있음 -> Stadium.location_no(JoinColumn 속성값) = StadiumImage.location_no
//    private LocationEntity location;
//
//    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
//    @JoinColumn(name = "stadium_no")
//    private List<StadiumImageEntity> images;
//
//    public static StadiumEntity from(Stadium stadium){
//        StadiumEntity stadiumEntity = new StadiumEntity();
//        stadiumEntity.stadiumNo=stadium.getStadiumNo();
//        stadiumEntity.stadiumName = stadium.getStadiumName();
//        stadiumEntity.location = LocationEntity.from(stadium.getLocation());
//        stadiumEntity.images= stadium.getImageList().stream().map(StadiumImage->StadiumImageEntity.from(StadiumImage)).collect(Collectors.toList());
//        return stadiumEntity;
//    }
//
//    public Stadium toModel(){
//        return Stadium.builder()
//                .stadiumNo(stadiumNo)
//                .stadiumName(stadiumName)
//                .imageList(images.stream().map(StadiumImageEntity::toModel).collect(Collectors.toList()))
//                .location(location.toModel())
//                .build()
//                ;
//    }
//}

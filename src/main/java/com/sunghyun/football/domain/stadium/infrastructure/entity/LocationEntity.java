//package com.sunghyun.football.domain.stadium.infrastructure.entity;
//
//import com.sunghyun.football.domain.stadium.domain.Location;
//import jakarta.persistence.*;
//
//@Table(name="stadium_location")
//@Entity
//public class LocationEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long locationNo;
//
//    private String province;
//
//    private String city;
//
//    private String address;
//
////    @OneToOne
////    private StadiumEntity stadium;
//
//    public static LocationEntity from(Location location){
//        LocationEntity locationEntity = new LocationEntity();
//        locationEntity.locationNo= location.getLocationNo();
//        locationEntity.province = location.getProvince();
//        locationEntity.city=location.getCity();
//        locationEntity.address=location.getAddress();
////        locationEntity.stadium= StadiumEntity.from(location.getStadium());
//        return locationEntity;
//    }
//
//    public Location toModel(){
//        return Location.builder()
//                .locationNo(locationNo)
//                .province(province)
//                .city(city)
//                .address(address)
////                .stadium(stadium.toModel())
//                .build()
//                ;
//    }
//}
